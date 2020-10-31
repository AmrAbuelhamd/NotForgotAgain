package com.blogspot.soyamr.notforgotagain.view.addnote

import com.blogspot.soyamr.notforgotagain.model.tables.Note

class AddNotePresenter(val addNoteView: AddNoteView) : AddNoteInteractor.OnLoginFinishedListener {
    private val addNoteInteractor = AddNoteInteractor(addNoteView.getRequiredContext(), this)

    fun signIn(email: String, password: String) {
//        signInInteractor.signIn(email, password)
//        signInView.showProgressBar()
    }

    override fun onSignInError() {
        addNoteView.apply {
//            addNoteView()
//            hidProgressBar()
        }
    }

    override fun onSuccess(userId: Long) {
        addNoteView.moveToNoteBoard(userId)
//        signInView.hidProgressBar()
    }

    override fun populateCategorySpinner(categories: ArrayList<String>) {
        addNoteView.populateCategorySpinnerData(categories)
    }

    override fun populatePrioritySpinner(priorities: ArrayList<String>) {
        addNoteView.populatePrioritySpinnerData(priorities)
    }

    override fun addNewNote(note: Note) {
        addNoteView.addNewNote(note)
        addNoteView.moveToNoteBoard(note.uid)
    }

    override fun error() {
        addNoteView.showError()
    }


    fun getSpinnersData() {
        addNoteInteractor.fetchCategories()
        addNoteInteractor.fetchPriorities()
    }

    fun addNewCategory(currentUserId: Long?, newCategory: String?) {
        addNoteInteractor.addNewCategory(currentUserId, newCategory)
    }

    fun saveNote(
        currentUserId: Long?,
        categorySpinner: String,
        prioritySpinner: String,
        dateText: String,
        headerTextLayout: String,
        descriptionTextLayout: String
    ) {
        addNoteInteractor.addNewNote(
            currentUserId, categorySpinner, prioritySpinner, dateText, headerTextLayout, descriptionTextLayout
        )

    }


}