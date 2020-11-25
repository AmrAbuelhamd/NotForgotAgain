package com.blogspot.soyamr.notforgotagain.view.addnote

import android.content.Context
import com.blogspot.soyamr.notforgotagain.domain.GeneralData
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.model.db.tables.Note

class AddNoteInteractor(
    private val context: Context,
    private val listener: OnLoginFinishedListener
) {
    interface OnLoginFinishedListener {
        fun onSignInError()
        fun onSuccess(userId: Long)
        fun populateCategorySpinner(data: List<GeneralData>)
        fun populatePrioritySpinner(priorities: List<GeneralData>)
        fun addNewNote(note: Note)
        fun error()
    }

    private val repository = NoteRepository(context)


    fun fetchCategories() {
//        val categories = repository.getCategories().value
//        val categoriesDomain = categories?.map { it.toDomain() }
//        listener.populateCategorySpinner(categoriesDomain!!)
    }

    fun fetchPriorities() {
//        val priorities = repository.getPriorities()
//        val prioritiesDomain = priorities.map { it.toDomain() }
//        listener.populatePrioritySpinner(prioritiesDomain)
    }

    fun addNewCategory(newCategory: String) {

        val category = repository.addNewCategory(newCategory)
        val categoryDomain = category.toDomain()
        listener.populateCategorySpinner(listOf(categoryDomain))
    }

    fun addNewNote(
        currentUserId: Long?,
        categorySpinner: Long,
        prioritySpinner: Long,
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
            listener.onSuccess(-1)
        }
    }

}