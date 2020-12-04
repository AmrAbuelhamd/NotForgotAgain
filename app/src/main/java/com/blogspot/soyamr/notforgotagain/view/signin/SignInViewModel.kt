package com.blogspot.soyamr.notforgotagain.view.signin

import androidx.lifecycle.*
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.model.Result
import com.blogspot.soyamr.notforgotagain.model.net.pojo.LoginUser
import com.blogspot.soyamr.notforgotagain.view.SingleLiveEvent
import com.blogspot.soyamr.notforgotagain.view.util.isValidEmail
import com.blogspot.soyamr.notforgotagain.view.util.isValidPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Override ViewModelProvider.NewInstanceFactory to create the ViewModel (VM).
class SignInViewModelFactory(private val repository: NoteRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = SignInViewModel(repository) as T
}

class SignInViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _passwordErrorMessage = MutableLiveData("")
    private val _emailErrorMessage = MutableLiveData("")
    private val _isLoading = MutableLiveData(true)
    private val _errorMessageSnakeBar = MutableLiveData("")

    val emailText = MutableLiveData("")
    val passwordText = MutableLiveData("")


    val emailErrorMessage: LiveData<String> = _emailErrorMessage
    val passwordErrorMessage: LiveData<String> = _passwordErrorMessage
    val isLoading: LiveData<Boolean> = _isLoading
    val doWeHaveAlreadySignedInUser = SingleLiveEvent<Boolean>()

    val errorMessageSnakeBar: LiveData<String> = _errorMessageSnakeBar

    init {
        doWeHaveSignedInUser()
    }

    private fun doWeHaveSignedInUser() {
        viewModelScope.launch(Dispatchers.Main) {
            _isLoading.value = true
            val ans = repository.doWeHaveToken()
            doWeHaveAlreadySignedInUser.value = ans
            _isLoading.value = false
        }
    }

    fun logIn() {
        viewModelScope.launch() {
            if (isValidInput()) {
                _isLoading.value = true
                when (val result =
                    repository.logIn(LoginUser(emailText.value!!, passwordText.value!!))) {
                    is Result.Success<Boolean> -> {
                        doWeHaveAlreadySignedInUser.value = result.data!!
                    }
                    else -> {
                        _errorMessageSnakeBar.value = (result as Result.Error).exception.message
                    }
                }
                _isLoading.value = false
            }
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

        if (!passwordText.value.isValidPassword()) {
            _passwordErrorMessage.value =
                "Invalid Password, password should contain {digit, lower case, upper case, no whitespace, 4 digits at least}"
            //result = false
        } else {
            _passwordErrorMessage.value = ""
        }
        println("resultt $result")
        return result
    }
}