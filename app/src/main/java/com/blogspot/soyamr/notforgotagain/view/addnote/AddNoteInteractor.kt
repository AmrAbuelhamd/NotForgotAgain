package com.blogspot.soyamr.notforgotagain.view.addnote

import android.content.Context
import android.util.Log
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.model.tables.Note

class AddNoteInteractor(
    private val context: Context,
    private val listener: OnLoginFinishedListener
) {
    interface OnLoginFinishedListener {
        fun onSignInError()
        fun onSuccess(userId: Long)
        fun populateCategorySpinner(data: ArrayList<String>)
        fun populatePrioritySpinner(priorities: ArrayList<String>)
        fun addNewNote(note: Note)
        fun error()
    }

    private val repository = NoteRepository(context)


    fun fetchCategories() {
        val categories = repository.getCategories()
        val data = ArrayList<String>()
        for (el in categories)
            data.add(el.name.toString())
        listener.populateCategorySpinner(data)
    }

    fun fetchPriorities() {
        val priorities = repository.getPriorities()
        val data = ArrayList<String>()
        for (el in priorities) {
            data.add(el.name.toString())
            Log.i("spinneri", el.name.toString())
        }
        listener.populatePrioritySpinner(data)
    }

    fun addNewCategory(currentUserId: Long?, newCategory: String?) {
        repository.addNewCategory(currentUserId, newCategory)
        val category = ArrayList<String>()
        category.add(newCategory.toString())
        listener.populateCategorySpinner(category)
    }

    fun addNewNote(
        currentUserId: Long?,
        categorySpinner: String,
        prioritySpinner: String,
        dateText: String,
        headerTextLayout: String,
        descriptionTextLayout: String
    ) {

        if (headerTextLayout.isEmpty() || descriptionTextLayout.isEmpty())
            listener.error()
        else {
            val note = repository.addNewNote(
                currentUserId,
                categorySpinner,
                prioritySpinner,
                dateText,
                headerTextLayout,
                descriptionTextLayout
            )
            listener.addNewNote(note)
        }
    }

}