package com.blogspot.soyamr.notforgotagain.view.noteboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.view.signin.SignInViewModel

class NoteBoardViewModelFactory(private val repository: NoteRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = NoteBoardViewModel(repository) as T
}

class NoteBoardViewModel(private val repository: NoteRepository) : ViewModel()  {


    fun logOutUser(){
        repository.logOutUser()
    }
}