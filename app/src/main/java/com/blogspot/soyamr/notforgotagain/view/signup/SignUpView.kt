package com.blogspot.soyamr.notforgotagain.view.signup

import android.content.Context

interface SignUpView {
    fun getRequiredContext(): Context
    fun goToLogInScreen()
    fun setSignUpError()

}