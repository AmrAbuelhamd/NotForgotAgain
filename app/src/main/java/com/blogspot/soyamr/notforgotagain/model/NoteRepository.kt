package com.blogspot.soyamr.notforgotagain.model

import android.content.Context
import android.util.Log
import com.blogspot.soyamr.notforgotagain.model.tables.*


object NoteRepository {

    private lateinit var noteDao: NoteDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var priorityDao: PriorityDao

    operator fun invoke(context: Context): NoteRepository {
        val db = NotesDataBase.getDatabase(context)
        noteDao = db.noteDao()
        categoryDao = db.categoryDao()
        priorityDao = db.priorityDao()
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
            headerTextLayout, descriptionTextLayout, false,5245, 45564,
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
    fun addNewCategory(newCategory: String):Category {
        val category = Category((++catNo).toLong(), newCategory)
        categoryDao.insertCategory(category)
        return category;
    }


}