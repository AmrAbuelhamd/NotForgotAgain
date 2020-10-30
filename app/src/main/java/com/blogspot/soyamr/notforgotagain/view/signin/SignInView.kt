package com.blogspot.soyamr.notforgotagain.view.signin

import android.content.Context

interface SignInView {
    fun getRequiredContext(): Context
    fun moveToNoteBoard(userId: Long)
    fun setSignInError()
    fun showProgressBar()
    fun hidProgressBar()
}