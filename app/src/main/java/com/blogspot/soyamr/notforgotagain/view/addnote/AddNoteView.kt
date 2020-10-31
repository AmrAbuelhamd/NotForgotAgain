package com.blogspot.soyamr.notforgotagain.view.addnote

import android.content.Context
import com.blogspot.soyamr.notforgotagain.model.tables.Note

interface AddNoteView {
    fun getRequiredContext(): Context
    fun moveToNoteBoard(userId: Long)
    fun setSignInError()
    fun showProgressBar()
    fun hidProgressBar()
    fun populateCategorySpinnerData(categories: ArrayList<String>)
    fun populatePrioritySpinnerData(priorities: ArrayList<String>)
    fun addNewNote(note: Note)
    fun showError()

}