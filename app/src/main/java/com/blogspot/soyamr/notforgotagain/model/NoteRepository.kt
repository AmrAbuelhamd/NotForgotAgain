package com.blogspot.soyamr.notforgotagain.model

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.domain.NoteBoss
import com.blogspot.soyamr.notforgotagain.model.db.NotesDataBase
import com.blogspot.soyamr.notforgotagain.model.db.tables.*
import com.blogspot.soyamr.notforgotagain.model.net.Network
import com.blogspot.soyamr.notforgotagain.model.net.TaskApiService
import com.blogspot.soyamr.notforgotagain.model.net.pojo.LoginUser
import com.blogspot.soyamr.notforgotagain.model.net.pojo.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

object NoteRepository {

    private val tag = "NoteRepository"
    private lateinit var noteDao: NoteDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var priorityDao: PriorityDao
    private lateinit var apiService: TaskApiService
    private lateinit var sharedPref: SharedPreferences
    private lateinit var context: Context

    operator fun invoke(context: Context): NoteRepository {
        this.context = context
        val db = NotesDataBase.getDatabase(context)
        apiService = Network.retrofit

        noteDao = db.noteDao()
        categoryDao = db.categoryDao()
        priorityDao = db.priorityDao()

        sharedPref = context.getSharedPreferences(
            context.resources.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )

        return this
    }

    fun getPriorities() = priorityDao.getAll()
    suspend fun getCategories() = categoryDao.getAll()

    //    fun addNewCategory( newCategory: String?) {
//        val category = Category(newCategory, categoryDao.getBiggestId() + 1)
//        categoryDao.insertCategory(category)
//    }

    fun addNewNote(
        currentUserId: Long?,
        categorySpinnerTextId: Long,
        prioritySpinnerTextId: Long,
        dateText: String,
        headerTextLayout: String,
        descriptionTextLayout: String
    ): Note {

        val note = Note(
            (++catNo).toLong(),
            headerTextLayout, descriptionTextLayout, false, 5245, 45564,
            categorySpinnerTextId, prioritySpinnerTextId
        )

        noteDao.insertNote(listOf(note))

        return note

    }

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
                Result.Error(Exception("AAmr Can't fetch data from local cash"))
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
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
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
    suspend fun updateDataBase(): Result<String> {
        return withContext(Dispatchers.IO) {
            if (isOnline()) {
                //get un Submitted notes and send them to api
//                try {
//                    val unSubmittedNotes = noteDao.getUnSubmittedNotes()
//                    val unSubmittedCats = categoryDao.getUnSubmittedCategories()
//                    if (unSubmittedCats.isNotEmpty() || unSubmittedNotes.isNotEmpty()) {
//                    }
//                    //todo create list of notes for server
//                    //todo send them to api
//
//                } catch (e: Exception) {
//                    println("$tag ->> $e")
//                    Result.Error(Exception("can't update remove serve"))
//                }

                //get data from api and send them to database [let's say there will be no problem from database]
                try {
                    val updatedTasks = apiService.getTasks()
//                    println("AAmr updatedTasks: $updatedTasks")
                    val cats = updatedTasks.map { it.toDataBaseCategory() }
                    val priority = updatedTasks.map { it.toDataBasePriority() }
                    val notes = updatedTasks.map { it.toDataBaseNote() }
                    cleanDataBase()
                    categoryDao.insertCategory(cats)
                    priorityDao.insertPriority(priority)
                    noteDao.insertNote(notes)
                    Result.Success("everything is good")
                } catch (e: Exception) {
//                    println("AAmr $tag ->> $e")
                    Result.Error(Exception("can't fetch data from server"))
                }
            } else {
                Result.Error(Exception("no internet"))
            }
        }
    }

    private fun cleanDataBase() {
        noteDao.deleteAll()
        categoryDao.deleteAll()
        priorityDao.deleteAll()
    }

    fun getUnSubmittedNotes() = noteDao.getUnSubmittedNotes()

    fun getnote(currentNote: Long?): Note {
        return noteDao.getNote(currentNote!!)
    }

    fun getCategory(cid: Long): Category {
        return categoryDao.getCategory(cid)
    }

    suspend fun getFullNoteDataRelatedToCategory(categoryId: Long) =
        noteDao.getFullNoteDataRelatedToCategory(categoryId)

    fun getFullNoteData(currentNoteId: Long) =
        noteDao.getFullNoteData(currentNoteId)

    var catNo: Int = 1;
    fun addNewCategory(newCategory: String): Category {
        val category = Category((++catNo).toLong(), newCategory)
        categoryDao.insertCategory(listOf(category))
        return category;
    }

    suspend fun logIn(loginUser: LoginUser): Boolean {

        val userToken =
            apiService.login(loginUser)

        if (userToken.apiToken.isNotEmpty()) {
            Network.updateToken(userToken.apiToken)
            with(sharedPref.edit()) {
                putString(context.resources.getString(R.string.tokenKey), userToken.apiToken)
                apply()
            }
        } else {
            return false
        }
        return true
    }

    fun doWeHaveToken(): Boolean {
        val defaultToken = context.resources.getString(R.string.defaultToken)
        val storedToken = sharedPref.getString(
            context.resources.getString(R.string.tokenKey),
            defaultToken
        )
        val result = storedToken != defaultToken
        if (storedToken != null && result)
            Network.updateToken(storedToken)

        return result
    }

    fun getCategoriesNet(): List<com.blogspot.soyamr.notforgotagain.model.net.pojo.Category>? {
        var categories1: List<com.blogspot.soyamr.notforgotagain.model.net.pojo.Category>? =
            null
        GlobalScope.launch(Dispatchers.Main) {
            val categories = withContext(Dispatchers.IO) {
                try {
                    apiService.getCategories()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("error cat ", e.toString())
                    null
                }
            } ?: return@launch
            Log.e("categoriess ", " " + categories.toString())
            categories1 = categories;
        }
        return categories1;

    }

    fun getNotesNet(): List<Task>? {
        var nots1: List<com.blogspot.soyamr.notforgotagain.model.net.pojo.Task>? = null
        GlobalScope.launch(Dispatchers.Main) {
            val nots = withContext(Dispatchers.IO) {
                try {
                    apiService.getTasks()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("error tasks ", e.toString())
                    null
                }
            } ?: return@launch
            Log.e("taskss ", " " + nots.toString())
            nots1 = nots;
        }
        return nots1;
    }

    suspend fun logOutUser() =
        withContext(Dispatchers.IO) {
            context.deleteDatabase("notes_database")
            val defaultToken = context.resources.getString(R.string.defaultToken)
            with(sharedPref.edit()) {
                putString(context.resources.getString(R.string.tokenKey), defaultToken)
                apply()
            }
        }


    fun getLiveNotes() =
        noteDao.getAll()

    //in case user is offline change locally and say nothing.
    //first check internet YES modify there then locally if no show error
    suspend fun markNoteAsDone(noteId: Long, checked: Boolean): Result<String> =
        withContext(Dispatchers.IO) {
            val note = noteDao.getNote(noteId)
            if(note.done){
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
                    return@withContext Result.Error(Exception("network problem, $e"))
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
}