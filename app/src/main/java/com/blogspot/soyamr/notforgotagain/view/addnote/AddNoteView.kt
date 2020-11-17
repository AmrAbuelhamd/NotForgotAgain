package com.blogspot.soyamr.notforgotagain.view.addnote

import android.content.Context
import com.blogspot.soyamr.notforgotagain.domain.GeneralData
import com.blogspot.soyamr.notforgotagain.model.tables.Note

interface AddNoteView {
    fun getRequiredContext(): Context
    fun moveToNoteBoard(userId: Long)
    fun setSignInError()
    fun showProgressBar()
    fun hidProgressBar()
    fun populateCategorySpinnerData(categories: List<GeneralData>)
    fun populatePrioritySpinnerData(priorities: List<GeneralData>)
    fun addNewNote(note: Note)
    fun showError()

}