package com.blogspot.soyamr.notforgotagain.view.noteboard

import android.util.Log
import androidx.lifecycle.*
import com.blogspot.soyamr.notforgotagain.domain.NoteBoss
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.model.Result
import kotlinx.coroutines.launch

class NoteBoardViewModelFactory(private val repository: NoteRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        NoteBoardViewModel(repository) as T
}

class NoteBoardViewModel(private val repository: NoteRepository) : ViewModel() {
    private val tag = "NoteBoardViewModel"
    private val _isLoading = MutableLiveData(false)
    private val _notes: MutableLiveData<List<NoteBoss>> = MutableLiveData()
    val notes: LiveData<List<NoteBoss>> = _notes
    val isLoading: LiveData<Boolean> = _isLoading


    init {
        updateDataBase()
        repository.getLiveNotes().observeForever(Observer { myNotes ->
            getNotes()
        })

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
                    println("AAmr _notes.value: ${_notes.value}")
                    println("AAmr result.data.value: ${result.data}")
                }
                else -> {
                    println("AAmr problem getting notes")
                    //show error}
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
                    Log.i(tag, result.toString())
                    //show error}
                }
            }
            _isLoading.value = false
        }
    }
}