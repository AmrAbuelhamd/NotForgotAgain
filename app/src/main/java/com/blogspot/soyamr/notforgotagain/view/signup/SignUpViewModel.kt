package com.blogspot.soyamr.notforgotagain.view.signup

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.model.Result
import com.blogspot.soyamr.notforgotagain.view.SingleLiveEvent
import com.blogspot.soyamr.notforgotagain.view.util.isValidEmail
import com.blogspot.soyamr.notforgotagain.view.util.isValidPassword
import kotlinx.coroutines.launch

//class SignUpViewModelFactory(
//    private val repository: NoteRepository
//) :
//    ViewModelProvider.NewInstanceFactory() {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
//        SignUpViewModel(repository) as T
//}

class SignUpViewModel @ViewModelInject constructor(val repository: NoteRepository) : ViewModel() {
    private val tag = "SignUpViewModel"
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    private val _success = SingleLiveEvent<Boolean>()
    val success: LiveData<Boolean> = _success

    private val _passwordErrorMessage = MutableLiveData("")
    private val _repeatPasswordErrorMessage = MutableLiveData("")
    private val _emailErrorMessage = MutableLiveData("")
    private val _nameErrorMessage = MutableLiveData("")

    val nameText = MutableLiveData("")
    val emailText = MutableLiveData("")
    val passwordText = MutableLiveData("")
    val repeatPasswordText = MutableLiveData("")


    val nameErrorMessage: LiveData<String> = _nameErrorMessage
    val emailErrorMessage: LiveData<String> = _emailErrorMessage
    val repeatPasswordErrorMessage: LiveData<String> = _repeatPasswordErrorMessage
    val passwordErrorMessage: LiveData<String> = _passwordErrorMessage


    private val _errorMessageSnakeBar = MutableLiveData("")
    val errorMessageSnakeBar: LiveData<String> = _errorMessageSnakeBar


    fun signUp() {
        viewModelScope.launch {
            _isLoading.value = true
            if (isValidInput()) {
                when (val result =
                    repository.signUp(nameText.value!!, emailText.value!!, passwordText.value!!)) {
                    is Result.Success<String> -> {
                        _success.value = true
                    }
                    else -> {
                        _errorMessageSnakeBar.value = (result as Result.Error).exception.message
                    }
                }
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

        if (!passwordText.value.isValidPassword()) {
            _passwordErrorMessage.value =
                "Invalid Password, password should contain {digit, lower case, upper case, no whitespace, 4 digits at least}"
            result = false
        } else {
            _passwordErrorMessage.value = ""
        }

        if (repeatPasswordText.value != passwordText.value) {
            _repeatPasswordErrorMessage.value =
                "passwords don't match"
            result = false
        } else {
            _repeatPasswordErrorMessage.value = ""
        }

        if (nameText.value.isNullOrEmpty()) {
            _nameErrorMessage.value =
                "write your name please"
            result = false
        } else {
            _nameErrorMessage.value = ""
        }

        return result
    }


}