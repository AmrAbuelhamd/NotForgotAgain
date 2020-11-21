package com.blogspot.soyamr.notforgotagain.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.blogspot.soyamr.notforgotagain.R
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


object NoteRepository {

    private lateinit var noteDao: NoteDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var priorityDao: PriorityDao
    private lateinit var apiService: TaskApiService
    private lateinit var sharedPref: SharedPreferences

    operator fun invoke(context: Context): NoteRepository {
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
    fun getCategories() = categoryDao.getAll()

    //    fun addNewCategory( newCategory: String?) {
//        val category = Category(newCategory, categoryDao.getBiggestId() + 1)
//        categoryDao.insertCategory(category)
//    }
//
    fun addNewNote(
        currentUserId: Long?,
        categorySpinnerTextId: Long,
        prioritySpinnerTextId: Long,
        dateText: String,
        headerTextLayout: String,
        descriptionTextLayout: String
    ): Note {
        Log.i("hie: ", " )))) " + categorySpinnerTextId)
        Log.i("hie: ", " )))) " + prioritySpinnerTextId)

        val note = Note(
            (++catNo).toLong(),
            headerTextLayout, descriptionTextLayout, false, 5245, 45564,
            categorySpinnerTextId, prioritySpinnerTextId
        )

        noteDao.insertNote(note)

        return note

    }

    fun getnotes(): List<Note> {
        return noteDao.getAll()
    }

    fun getnote(currentNote: Long?): Note {
        return noteDao.getNote(currentNote!!)
    }

    fun getCategory(cid: Long): Category {
        return categoryDao.getCategory(cid)
    }

    fun getFullNoteDataRelatedToCategory(categoryId: Long) =
        noteDao.getFullNoteDataRelatedToCategory(categoryId)

    fun getFullNoteData(currentNoteId: Long) =
        noteDao.getFullNoteData(currentNoteId)

    var catNo: Int = 1;
    fun addNewCategory(newCategory: String): Category {
        val category = Category((++catNo).toLong(), newCategory)
        categoryDao.insertCategory(category)
        return category;
    }

    fun logIn(loginUser: LoginUser) {
        GlobalScope.launch(Dispatchers.Main) {
            val userToken = withContext(Dispatchers.IO) {
                try {
                    apiService.login(loginUser)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("error login ", e.toString())

                    null
                }
            } ?: return@launch
            Network.updateToken(userToken)
            Log.e("Token ", " " + userToken)
        }
    }


    fun getCategoriesNet(): List<com.blogspot.soyamr.notforgotagain.model.net.pojo.Category>? {
        var categories1: List<com.blogspot.soyamr.notforgotagain.model.net.pojo.Category>? = null
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
}