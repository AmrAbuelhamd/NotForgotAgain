package com.blogspot.soyamr.notforgotagain.view.addnote

import androidx.lifecycle.*
import com.blogspot.soyamr.notforgotagain.domain.GeneralData
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.model.db.tables.Category
import com.blogspot.soyamr.notforgotagain.model.db.tables.Priority
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class AddNoteViewModelFactory(
    private val repository: NoteRepository,
    private val noteId: Long
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        AddNoteViewModel(repository, noteId) as T
}

class AddNoteViewModel(private val repository: NoteRepository, private val noteId: Long) :
    ViewModel() {
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<GeneralData>> = _categories.map { it.map { it.toDomain() } }

    private val _priorities = MutableLiveData<List<Priority>>()
    val priorities: LiveData<List<GeneralData>> = _priorities.map { it.map { it.toDomain() } }


    private val note = repository.getFullNoteData(noteId)

    val toolbarTitle = if (noteId == -1L) "Добавить заметку" else "Изменить заметку"
    val titleText = MutableLiveData(note?.nTitle)
    val descText = MutableLiveData(note?.nDescription)
    val dateText = MutableLiveData(makeDataString())


    init {
        getCats()
        getPriority()
    }

    private fun getCats() {
        viewModelScope.launch {
            _categories.value = repository.getCategories()
        }
    }

    private fun getPriority() {
        viewModelScope.launch {
            _priorities.value = repository.getPriorities()
        }
    }

    private fun makeDataString(): String {
        var formattedDate: String
        try {
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            val netDate = Date(note.nDeadline!! * 1000)
            formattedDate = sdf.format(netDate)
            val deadline = sdf.parse(formattedDate)
            val current = sdf.parse(sdf.format(Calendar.getInstance().time))
            if (deadline!!.after(current))
                return "До $formattedDate"
            return "Был в $formattedDate"


        } catch (e: Exception) {
            return ""
        }
    }

}