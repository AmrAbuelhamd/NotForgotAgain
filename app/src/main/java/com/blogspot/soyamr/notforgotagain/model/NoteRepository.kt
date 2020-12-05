package com.blogspot.soyamr.notforgotagain.model

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.domain.NoteBoss
import com.blogspot.soyamr.notforgotagain.model.db.NotesDataBase
import com.blogspot.soyamr.notforgotagain.model.db.tables.*
import com.blogspot.soyamr.notforgotagain.model.net.Network
import com.blogspot.soyamr.notforgotagain.model.net.TaskApiService
import com.blogspot.soyamr.notforgotagain.model.net.pojo.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import com.blogspot.soyamr.notforgotagain.model.db.tables.Category as dbCategory
import com.blogspot.soyamr.notforgotagain.model.db.tables.Priority as dbPriority
import com.blogspot.soyamr.notforgotagain.model.net.pojo.Category as NetCategory


//lidia perfect class used in goolge samples used to get the results
sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
//lidia dagger hilt injecting application context then using the repo itself everywhere needed.
@Singleton
class NoteRepository @Inject constructor(@ApplicationContext val context: Context) {

    private val tag = "NoteRepository"
    private val noteDao: NoteDao
    private val categoryDao: CategoryDao
    private val priorityDao: PriorityDao
    private val deletedNotesIdsDao: DeletedNotesIdsDao
    private val apiService: TaskApiService
    private val sharedPref: SharedPreferences

    init {
        val db = NotesDataBase.getDatabase(context)
        apiService = Network.retrofit

        noteDao = db.noteDao()
        categoryDao = db.categoryDao()
        priorityDao = db.priorityDao()
        deletedNotesIdsDao = db.deletedNotesIdsDao()

        sharedPref = context.getSharedPreferences(
            context.resources.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
    }

    suspend fun getPriorities() =
        withContext(Dispatchers.IO) { priorityDao.getAll() }

    suspend fun getCategories() =
        withContext(Dispatchers.IO) { categoryDao.getAll() }


    //first: get data from database, and make a ready to use list of noteBoss for recyclerview
    suspend fun getNotes(): Result<ArrayList<NoteBoss>> {
        return withContext(Dispatchers.IO) {
            try {
                val cats = categoryDao.getAll().toMutableList()
                cats.removeFirst()
                val notes = ArrayList<NoteBoss>()
                cats.forEach {
                    val notesOfCategory = noteDao.getFullNoteDataRelatedToCategory(it.id)
                    notes.add(NoteBoss(null, it))
                    notesOfCategory.forEach {
                        notes.add(NoteBoss(it, null))
                    }
                }
                Result.Success(notes)
            } catch (e: Exception) {
                Result.Error(Exception("Fatal Exception Can't fetch data from local cash \n{${e.message}}"))
            }
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
//                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
//                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
//                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
//        }
        return false
    }

    //1- check if there is internet or no -> IF no THEN return no internet mistake, IF yes THEN:
    //2- get un submitted notes and send them to api IF error THEN  return and show error "can't update remove server"
    // IF yes THEN get notes from server and store them to database, and return success
    suspend fun updateDataBase(): Result<String> =
        withContext(Dispatchers.IO) {
            if (isOnline()) {
                //get un Submitted notes and send them to api
                try {
                    //these are on server and need to be deleted first
                    val notesToDelete = deletedNotesIdsDao.getAll()
                    notesToDelete.forEach {
                        apiService.deleteNote(it.id.toInt())
                        deletedNotesIdsDao.delete(it)
                    }

                    val unSubmittedCats = categoryDao.getUnSubmittedCategories()
                    //send category, send notes related to that category, delete them
                    unSubmittedCats.forEach {
                        val serverCategory = apiService.addNewCategory(it.toNetPojo())
                        val notesRelatedToThisCategory =
                            noteDao.getFullNoteDataRelatedToCategory(it.id)
                        notesRelatedToThisCategory.forEach {
                            apiService.addNewTask(it.toNetTask(parentCategoryId = serverCategory.id))
                            noteDao.deleteNote(it.nId)
                        }
                        categoryDao.delete(it)
                    }
                    //these notes aren't connected to any offline category
                    val unSubmittedNotes = noteDao.getUnSubmittedNotes()
                    unSubmittedNotes.forEach {
                        val note = apiService.addNewTask(it.toNetPojoTask())
                        noteDao.delete(it)
                        noteDao.insertNote(listOf(note.toDataBaseNote()))
                    }
                } catch (e: Exception) {
                    println("$tag ->> $e")
                    return@withContext Result.Error(Exception("can't update remote server \n{${e.message.toString()}}"))
                }

                //get data from api and send them to database [let's say there will be no problem from database]
                try {
                    val updatedTasks = apiService.getTasks()
                    val updatedCats = apiService.getCategories()
                    val updatedPriorities = apiService.getPriorities()

                    val cats = updatedCats.map { it.toDataBaseCategory() }
                    val priority = updatedPriorities.map { it.toDataBasePriority() }
                    val notes = updatedTasks.map { it.toDataBaseNote() }
                    cleanDataBase()
                    categoryDao.insertCategory(cats)
                    priorityDao.insertPriority(priority)
                    noteDao.insertNote(notes)
                    return@withContext Result.Success("everything is good")
                } catch (e: Exception) {
//                    println("AAmr $tag ->> $e")
                    return@withContext Result.Error(Exception("can't fetch data from server ${e.message.toString()}"))
                }
            } else {
                return@withContext Result.Error(Exception("no internet, this data comes from local cash"))
            }
        }

    private fun cleanDataBase() {
        noteDao.deleteAll()
        categoryDao.deleteAll()
        priorityDao.deleteAll()
        deletedNotesIdsDao.deleteAll()
        // context.deleteDatabase("notes_database")
    }


    suspend fun getCategory(cid: Long): dbCategory = withContext(Dispatchers.IO) {
        categoryDao.getCategory(cid)
    }

    suspend fun getPriority(pid: Long): dbPriority = withContext(Dispatchers.IO) {
        priorityDao.getPriority(pid)
    }

    suspend fun getFullNoteData(currentNoteId: Long) = withContext(Dispatchers.IO) {
        noteDao.getFullNoteData(currentNoteId)
    }

    suspend fun addNewCategory(newCategoryString: String): Result<dbCategory> =
        withContext(Dispatchers.IO) {
            if (isOnline()) {
                val newCategory = NewCategory(newCategoryString)
                val result: NetCategory
                try {
                    result = apiService.addNewCategory(newCategory)
                } catch (e: Exception) {
                    return@withContext Result.Error(Exception("network problem, ${e.message.toString()}"))
                }
                val cat = result.toDataBaseCategory()
                categoryDao.insertCategory(listOf(cat))
                return@withContext Result.Success(cat)
            } else {
                val id = categoryDao.getMaxId() + 1
                val offlineCategory = dbCategory(id, newCategoryString, false)
                categoryDao.insertCategory(listOf(offlineCategory))
                return@withContext Result.Success(offlineCategory)
            }
        }

    suspend fun logIn(loginUser: LoginUser): Result<Boolean> =
        withContext(Dispatchers.IO) {
            if (isOnline()) {
                try {
                    val userToken = apiService.login(loginUser)
                    if (userToken.apiToken.isNotEmpty()) {
                        Network.updateToken(userToken.apiToken)
                        with(sharedPref.edit()) {
                            putString(
                                context.resources.getString(R.string.tokenKey),
                                userToken.apiToken
                            )
                            apply()
                        }
                        return@withContext Result.Success(true)
                    } else {
                        return@withContext Result.Error(java.lang.Exception("unknown error"))
                    }
                } catch (e: java.lang.Exception) {
                    return@withContext Result.Error(Exception("wrong email or password \n{${e.message}}"))
                }
            } else {
                return@withContext Result.Error(java.lang.Exception("you are offline"))
            }
        }

    suspend fun doWeHaveToken(): Boolean = withContext(Dispatchers.IO) {
        val defaultToken = context.resources.getString(R.string.defaultToken)
        val storedToken = sharedPref.getString(
            context.resources.getString(R.string.tokenKey),
            defaultToken
        )
        val result = storedToken != defaultToken
        if (storedToken != null && result)
            Network.updateToken(storedToken)

        return@withContext result
    }

    suspend fun logOutUser() =
        withContext(Dispatchers.IO) {
            cleanDataBase()
//            context.deleteDatabase("notes_database")
            val defaultToken = context.resources.getString(R.string.defaultToken)
            with(sharedPref.edit()) {
                putString(context.resources.getString(R.string.tokenKey), defaultToken)
                apply()
            }
        }


    suspend fun getLiveNotes() = withContext(Dispatchers.IO) {
        noteDao.getAll()
    }

    //in case user is offline change locally and say nothing.
    //first check internet YES modify there then locally if no show error
    suspend fun markNoteAsDone(noteId: Long, checked: Boolean): Result<String> =
        withContext(Dispatchers.IO) {
            val note = noteDao.getNote(noteId)
            if (note.done) {
                return@withContext Result.Success("everything is good offline")
            }
            if (note.isSavedToApi == false) {
                if (checked) noteDao.setDone(noteId)
                return@withContext Result.Success("everything is good offline")
            }
            if (isOnline()) {
                val updatedTask = note.toUpdatedTask(done = if (checked) 1 else 0)
                val result: Task
                try {
                    result = apiService.updateTask(updatedTask, noteId)
                } catch (e: Exception) {
                    return@withContext Result.Error(Exception("network problem, {${e.message.toString()}}"))
                }
                val cat = result.toDataBaseCategory()
                if (!categoryDao.isRowExist(cat.id))
                    categoryDao.insertCategory(listOf(cat))
                val priority = result.toDataBasePriority()
                if (!priorityDao.isRowExist(priority.id))
                    priorityDao.insertPriority(listOf(priority))
                noteDao.delete(note)
                noteDao.insertNote(listOf(result.toDataBaseNote()))
                return@withContext Result.Success("everything is good")
            } else {
                return@withContext Result.Error(Exception("you are offline changes couldn't be saved"))
            }
        }

    suspend fun updateNote(
        noteId: Long,
        title: String,
        desc: String,
        deadline: Long,
        categorySpinnerId: Long,
        prioritySpinnerId: Long
    ): Result<String> = withContext(Dispatchers.IO) {
        if (isOnline()) {
            val note = noteDao.getNote(noteId)
            val updatedTask = note.toUpdatedTask(
                title,
                desc,
                deadline = deadline,
                parentCategoryId = categorySpinnerId,
                parentPriorityId = prioritySpinnerId
            )
            val result: Task
            try {
                result = apiService.updateTask(updatedTask, noteId)
            } catch (e: Exception) {
                return@withContext Result.Error(Exception("network problem, ${e.message.toString()}"))
            }
            noteDao.delete(note)
            noteDao.insertNote(listOf(result.toDataBaseNote()))
            return@withContext Result.Success("updates saved to api")
        } else {
            val note = noteDao.getNote(noteId)
            if (note.isSavedToApi == false) {
                noteDao.deleteNote(noteId)
                noteDao.insertNote(
                    listOf(
                        Note(
                            noteId,
                            title,
                            desc,
                            false,
                            deadline,
                            0,
                            categorySpinnerId,
                            prioritySpinnerId,
                            false
                        )
                    )
                )
                return@withContext Result.Success("everything is good offline")
            }
            return@withContext Result.Error(Exception("you are offline changes couldn't be saved"))
        }
    }

    suspend fun addNewNote(
        title: String,
        desc: String,
        deadline: Long,
        categorySpinnerId: Long,
        prioritySpinnerId: Long,
        created: Long
    ): Result<String> = withContext(Dispatchers.IO) {
        if (isOnline()) {
            val task = NewTask(title, desc, 0, deadline, categorySpinnerId, prioritySpinnerId)
            val result: Task
            try {
                println("task: $task")
                result = apiService.addNewTask(task)
            } catch (e: Exception) {
                return@withContext Result.Error(Exception("network problem, ${e.message.toString()}"))
            }
            noteDao.insertNote(listOf(result.toDataBaseNote(true)))
            return@withContext Result.Success("saved to api")
        } else {
            noteDao.insertNote(
                listOf(
                    Note(
                        getValidNoteId(), title, desc, false, deadline,
                        created, categorySpinnerId, prioritySpinnerId, false
                    )
                )
            )
            return@withContext Result.Success("Saved Locally")
        }
    }

    fun getValidNoteId(): Long =
        noteDao.getBiggestId() + 1L

    suspend fun signUp(name: String, email: String, password: String): Result<String> =
        withContext(Dispatchers.IO) {
            if (isOnline()) {
                val newUser = NewUser(email, name, password)
                val result: User
                try {
                    result = apiService.registerUser(newUser)
                    if (result.apiToken.isNotEmpty()) {
                        Network.updateToken(result.apiToken)
                        with(sharedPref.edit()) {
                            putString(
                                context.resources.getString(R.string.tokenKey),
                                result.apiToken
                            )
                            apply()
                        }
                    }
                } catch (e: Exception) {
                    return@withContext Result.Error(Exception("network problem, {$e}"))
                }
                //Network.updateToken(result.apiToken)
                return@withContext Result.Success("signed up successfully")
            } else {
                return@withContext Result.Error(Exception("you are offline"))
            }
        }

    suspend fun getLiveCats() = withContext(Dispatchers.IO) {
        categoryDao.getLiveAll()
    }

    suspend fun deleteNote(id: Long): Result<String> =
        withContext(Dispatchers.IO) {
            if (isOnline()) {
                try {
                    apiService.deleteNote(id.toInt())
                    noteDao.deleteNote(id)
                } catch (e: Exception) {
                    return@withContext Result.Error(Exception("network problem, $e"))
                }
                return@withContext Result.Success("note deleted successfully")
            } else {
                val note = noteDao.getNote(id)
                if (!note.isSavedToApi!!) {
                    noteDao.deleteNote(id)
                    return@withContext Result.Success("note deleted successfully")
                } else {
                    deletedNotesIdsDao.insert(DeletedNotesIds(id))
                    noteDao.deleteNote(id)
                    return@withContext Result.Success("note deleted offline")
                }
            }
        }
}