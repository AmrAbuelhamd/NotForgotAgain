package com.blogspot.soyamr.notforgotagain.view.noteboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blogspot.soyamr.notforgotagain.domain.NoteBoss
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.model.Result
import kotlinx.coroutines.launch

//class NoteBoardViewModelFactory(private val repository: NoteRepository) :
//    ViewModelProvider.NewInstanceFactory() {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
//        NoteBoardViewModel(repository) as T
//}

class NoteBoardViewModel @ViewModelInject constructor(private val repository: NoteRepository) : ViewModel() {
    private val tag = "NoteBoardViewModel"
    private val _isLoading = MutableLiveData(false)
    private val _notes: MutableLiveData<List<NoteBoss>> = MutableLiveData()
    val notes: LiveData<List<NoteBoss>> = _notes
    val isLoading: LiveData<Boolean> = _isLoading
    private val _errorMessageSnakeBar = MutableLiveData("")
    val errorMessageSnakeBar: LiveData<String> = _errorMessageSnakeBar

    init {
        updateDataBase()
    }

    fun logOutUser() {
        viewModelScope.launch() {
            _isLoading.value = true
            repository.logOutUser()
            _isLoading.value = false
        }
    }

    private fun getNotes() {
        viewModelScope.launch() {
            _isLoading.value = true
            when (val result = repository.getNotes()) {
                is Result.Success<ArrayList<NoteBoss>> -> {
                    _notes.value = result.data
                }
                else -> {
                    _errorMessageSnakeBar.value = (result as Result.Error).exception.message
                }
            }
            _isLoading.value = false
        }
    }

    private fun updateDataBase() {
        viewModelScope.launch() {
            _isLoading.value = true
            when (val result = repository.updateDataBase()) {
                is Result.Success<String> -> {
                    println("$tag fetched data")
                }
                else -> {
                    _errorMessageSnakeBar.value = (result as Result.Error).exception.message
                }
            }
            getNotes()
            _isLoading.value = false
        }
    }

    fun onCheckBoxClicked(noteId: Long, checked: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.markNoteAsDone(noteId, checked)) {
                is Result.Success<String> -> {
                    println("$tag fetched data")
                }
                else -> {
                    _errorMessageSnakeBar.value = (result as Result.Error).exception.message
                }
            }
            getNotes()
            _isLoading.value = false
        }
    }

    fun refresh() {
        updateDataBase()
    }

    fun removeItem(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.deleteNote(id)) {
                is Result.Success<String> -> {
                    println("$tag fetched data")
                }
                else -> {
                    _errorMessageSnakeBar.value = (result as Result.Error).exception.message
                    //show error}
                }
            }
            getNotes()
            _isLoading.value = false
        }
    }
}