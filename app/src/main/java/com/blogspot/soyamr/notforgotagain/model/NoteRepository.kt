package com.blogspot.soyamr.notforgotagain.model

import android.content.Context
import android.util.Log
import com.blogspot.soyamr.notforgotagain.model.tables.*


object NoteRepository {

    private lateinit var userDao: UserDao
    private lateinit var noteDao: NoteDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var priorityDao: PriorityDao

    operator fun invoke(context: Context): NoteRepository {
        val db = NotesDataBase.getDatabase(context)
        userDao = db.userDao()
        noteDao = db.noteDao()
        categoryDao = db.categoryDao()
        priorityDao = db.priorityDao()
        return this
    }

    fun getSignedInUser() = userDao.getSignedInUser()

    fun doWeHaveSuchUser(email: String, password: String) =
        userDao.doWeHaveSuchUser(email, password)

    fun signMeIn(uId: Long) {
        userDao.signInUser(uId)
    }

    fun signMeOut(currentUserId: Long) {
        userDao.signOutUser(currentUserId)
    }

    fun insetUser(name: String, email: String, password: String) {
        val user = User(name, email, password)
        userDao.insertUser(user)
    }


    fun getPriorities() = priorityDao.getAll()
    fun getCategories() = categoryDao.getAll()
    fun addNewCategory(currentUserId: Long?, newCategory: String?) {
        val category = Category(newCategory, categoryDao.getBiggestId() + 1)
        categoryDao.insertCategory(category)
    }

    fun addNewNote(
        currentUserId: Long?,
        categorySpinnerText: String,
        prioritySpinnerText: String,
        dateText: String,
        headerTextLayout: String,
        descriptionTextLayout: String
    ): Note {
        val categoryId = categoryDao.getCategoryId(categorySpinnerText)
        val priorityId = priorityDao.getPriorityId(prioritySpinnerText)

        Log.i("please2: ", " )))) " + currentUserId)

        val note = Note(
            headerTextLayout, descriptionTextLayout, dateText, false,
            currentUserId!!, categoryId, priorityId
        )

        noteDao.insertNote(note)

        return note

    }

    fun getnotes(currentUserId: Long?): List<Note> {
        return noteDao.getAll(currentUserId!!)
    }


}