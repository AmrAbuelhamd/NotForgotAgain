package com.blogspot.soyamr.notforgotagain.view.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.model.net.pojo.LoginUser
import com.blogspot.soyamr.notforgotagain.view.SingleLiveEvent
import com.blogspot.soyamr.notforgotagain.view.util.isValidEmail
import kotlinx.coroutines.*

// Override ViewModelProvider.NewInstanceFactory to create the ViewModel (VM).
class SignInViewModelFactory(private val repository: NoteRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = SignInViewModel(repository) as T
}

class SignInViewModel(private val repository: NoteRepository) : ViewModel() {

    val coroutineExceptionHanlder = CoroutineExceptionHandler { _, throwable ->
        throwable.stackTrace
        println("BIG PROBLEM ${throwable.stackTrace}")
    }


    private val _passwordErrorMessage = MutableLiveData("")
    private val _emailErrorMessage = MutableLiveData("")
    private val _isLoading = MutableLiveData(true)

    val emailText = MutableLiveData("")
    val passwordText = MutableLiveData("")


    val emailErrorMessage: LiveData<String> = _emailErrorMessage
    val passwordErrorMessage: LiveData<String> = _passwordErrorMessage
    val isLoading: LiveData<Boolean> = _isLoading
    val doWeHaveAlreadySignedInUser = SingleLiveEvent<Boolean>()


    init {
        doWeHaveSignedInUser()
    }

    fun doWeHaveSignedInUser() {
        GlobalScope.launch(Dispatchers.Main) {
            _isLoading.value = true
            val ans = withContext(Dispatchers.IO) {
                repository.doWeHaveToken()
            }
            _isLoading.value = false
            doWeHaveAlreadySignedInUser.value = ans
        }
    }

    fun logIn() {
        GlobalScope.launch(Dispatchers.Main) {
            _isLoading.value = true
            if (isValidInput()) {
                val result = withContext(Dispatchers.IO) {
                    try {
                        repository.logIn(LoginUser(emailText.value!!, passwordText.value!!))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        android.util.Log.e("error login ", e.toString())
                        false
                    }
                }
                doWeHaveAlreadySignedInUser.value = result
            }
            _isLoading.value = false
        }
    }

    private fun isValidInput(): Boolean {
        var result = true

        if (!emailText.value.isValidEmail()) {
            _emailErrorMessage.value = "Invalid Email"
            result = false
        } else {
            _emailErrorMessage.value = ""
        }

//        if (!passwordText.value.isValidPassword()) {
//            _passwordErrorMessage.value =
//                "Invalid Password, password should contain {digit, lower case, upper case, no whitespace, 4 digits at least}"
//            result = false
//        } else {
//            _passwordErrorMessage.value = ""
//        }
        println("resultt $result")
        return result
    }
}