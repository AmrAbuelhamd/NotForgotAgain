package com.blogspot.soyamr.notforgotagain.view.signup

import android.content.Context
import com.blogspot.soyamr.notforgotagain.model.NoteRepository

class SignUpInteractor(
    private val context: Context,
    private val listener: OnSignUpFinishedListener
) {
    private val repository = NoteRepository(context)

    interface OnSignUpFinishedListener {
        fun onSuccess()
    }

    fun insertUser(name: String, email: String, password: String) {
        repository.insetUser(name, email, password)
        listener.onSuccess()
    }

}