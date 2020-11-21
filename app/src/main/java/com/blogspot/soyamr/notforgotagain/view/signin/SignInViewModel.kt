package com.blogspot.soyamr.notforgotagain.view.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.model.net.pojo.LoginUser
import com.blogspot.soyamr.notforgotagain.view.util.isValidEmail
import com.blogspot.soyamr.notforgotagain.view.util.isValidPassword

// Override ViewModelProvider.NewInstanceFactory to create the ViewModel (VM).
class SignInViewModelFactory(private val repository: NoteRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = SignInViewModel(repository) as T
}

class SignInViewModel(val repository: NoteRepository) : ViewModel() {


    private val _passwordErrorMessage = MutableLiveData("")
    private val _emailErrorMessage = MutableLiveData("")

    val emailText = MutableLiveData("")
    val passwordText = MutableLiveData("")


    val emailErrorMessage: LiveData<String> = _emailErrorMessage
    val passwordErrorMessage: LiveData<String> = _passwordErrorMessage


    fun logIn() {
        if (isValideInput()) {
            repository.logIn(LoginUser(emailText.value!!, passwordText.value!!))
        }
    }

    private fun isValideInput(): Boolean {
        var result = true

        if (!emailText.value.isValidEmail()) {
            _emailErrorMessage.value = "Invalid Email"
            result = false
        } else {
            _emailErrorMessage.value = ""
        }

        if (!passwordText.value.isValidPassword()) {
            _passwordErrorMessage.value =
                "Invalid Password, password should contain {digit, lower case, upper case, no whitespace, 4 digits at least}"
            result = false
        } else {
            _passwordErrorMessage.value = ""
        }

        return result

    }
}