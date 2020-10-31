package com.blogspot.soyamr.notforgotagain.view.signup

class SignUpPresenter(private val signUpView: SignUpView) : SignUpInteractor.OnSignUpFinishedListener {
    private val signUpInteractor = SignUpInteractor(signUpView.getRequiredContext(),this)

    override fun onSuccess() {
        signUpView.goToLogInScreen()
    }

    override fun onError() {
        signUpView.setSignUpError()
    }

    fun insertUser(name: String, email: String, password: String, repeatPassword: String) {
        signUpInteractor.insertUser(name,email,password,repeatPassword)
    }

}