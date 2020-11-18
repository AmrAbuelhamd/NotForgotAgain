package com.blogspot.soyamr.notforgotagain.view.signin

import android.content.Context
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.model.net.pojo.LoginUser

class SignInInteractor(
    private val context: Context,
    private val listener: OnLoginFinishedListener
) {
    interface OnLoginFinishedListener {
        fun onSignInError()
        fun onSuccess(userId: Long)
    }

    private val repository = NoteRepository(context)

    fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty())
            listener.onSignInError()
        else {
            repository.logIn(LoginUser(email, password))

            listener.onSuccess(0)
        }
    }

    fun checkSignedIn() {
//        val id = repository.getSignedInUser()
//        Log.i("dIdSign ", "" + id)
//        if (id != 0L)
        listener.onSuccess(0)

    }

}