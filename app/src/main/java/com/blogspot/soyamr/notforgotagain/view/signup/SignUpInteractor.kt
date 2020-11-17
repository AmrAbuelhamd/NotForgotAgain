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
        fun onError()
    }

    fun insertUser(name: String, email: String, password: String, repeatPassword: String) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || repeatPassword.isEmpty()
            || password != repeatPassword
        ) {
            listener.onError()
        } else {
//            repository.insetUser(name, email, password)
            listener.onSuccess()
        }
    }

}