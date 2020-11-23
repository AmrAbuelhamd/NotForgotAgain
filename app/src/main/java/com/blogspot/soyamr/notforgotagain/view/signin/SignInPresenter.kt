package com.blogspot.soyamr.notforgotagain.view.signin

class SignInPresenter(val signInView: SignInView) : SignInInteractor.OnLoginFinishedListener {
    private val signInInteractor = SignInInteractor(signInView.getRequiredContext(),this)

    fun signIn(email: String, password: String) {
//        signInInteractor.signIn(email, password)
    }

    override fun onSignInError() {
        signInView.apply {
            setSignInError()
        }
    }

    override fun onSuccess(userId: Long) {
        signInView.moveToNoteBoard(userId)
    }

    fun checkSignedIn() {
        signInInteractor.checkSignedIn()
    }


}