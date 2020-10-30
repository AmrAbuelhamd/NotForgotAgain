package com.blogspot.soyamr.notforgotagain.view.signin

class SignInPresenter(val signInView: SignInView) : SignInInteractor.OnLoginFinishedListener {
    private val signInInteractor = SignInInteractor(signInView.getRequiredContext(),this)

    fun signIn(email: String, password: String) {
        signInInteractor.signIn(email, password)
//        signInView.showProgressBar()
    }

    override fun onSignInError() {
        signInView.apply {
            setSignInError()
//            hidProgressBar()
        }
    }

    override fun onSuccess(userId: Long) {
        signInView.moveToNoteBoard(userId)
//        signInView.hidProgressBar()
    }

    fun checkSignedIn() {
        signInInteractor.checkSignedIn()
    }


}