package com.blogspot.soyamr.notforgotagain.view.addnote

import androidx.lifecycle.*
import com.blogspot.soyamr.notforgotagain.domain.GeneralData
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.model.Result
import com.blogspot.soyamr.notforgotagain.model.db.tables.Category
import com.blogspot.soyamr.notforgotagain.model.db.tables.FullNoteData
import com.blogspot.soyamr.notforgotagain.model.db.tables.Priority
import com.blogspot.soyamr.notforgotagain.view.SingleLiveEvent
import kotlinx.coroutines.delay
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
    private val tag = "addnoteviewmodel"
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<GeneralData>> = _categories.map { it.map { it.toDomain() } }
    private val _priorities = MutableLiveData<List<Priority>>()

    val priorities: LiveData<List<GeneralData>> = _priorities.map { it.map { it.toDomain() } }
    private val _selectedCategory =
        MutableLiveData<Category>()

    private val _selectedPriority =
        MutableLiveData<Priority>()
    val selectedCategory: LiveData<GeneralData> =
        _selectedCategory.map { it.toDomain() }

    val selectedPriority: LiveData<GeneralData> =
        _selectedPriority.map { it.toDomain() }
    lateinit var note: FullNoteData

    private val _success = SingleLiveEvent<Boolean>()
    val success: LiveData<Boolean> = _success

    val toolbarTitle = if (noteId == -1L) "Добавить заметку" else "Изменить заметку"
    val titleText = MutableLiveData<String>()
    val descText = MutableLiveData<String>()
    val dateText = MutableLiveData<String>()

    private val _dateErrorMessage = MutableLiveData("")
    val dateErrorMessage: LiveData<String> = _dateErrorMessage
    private val _titleErrorMessage = MutableLiveData("")
    val titleErrorMessage: LiveData<String> = _titleErrorMessage
    private val _descErrorMessage = MutableLiveData("")
    val descErrorMessage: LiveData<String> = _descErrorMessage

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String> = _errorMessage

    private val _snackBarMessage = MutableLiveData("")
    val snackBarMessage: LiveData<String> = _snackBarMessage


    init {
        viewModelScope.launch {
            if (noteId != -1L) {
                note = repository.getFullNoteData(noteId)
                titleText.value = note.nTitle
                descText.value = note.nDescription!!
                dateText.value = makeDataString(note.nDeadline)
            }
            try {
                getCats()
                getPriority()
            }
            catch (e:java.lang.Exception){
                println("$tag ${e.message.toString()}")
            }
        }
    }

    private fun getCats() {
        viewModelScope.launch {
            _categories.value = repository.getCategories()
            if (noteId != -1L) {
                _selectedCategory.value = repository.getCategory(note.cId)
            }
        }
    }

    private fun getPriority() {
        viewModelScope.launch {
            _priorities.value = repository.getPriorities()
            if (noteId != -1L) {
                _selectedPriority.value = repository.getPriority(note.pId)
            }
        }
    }

    private fun makeDataString(nDeadline: Long?): String {
        val formattedDate: String
        if (nDeadline == null)
            return ""
        try {
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            val netDate = Date(nDeadline * 1000)
            formattedDate = sdf.format(netDate)
            return formattedDate
        } catch (e: Exception) {
            return ""
        }
    }

    fun addNewCategory(newCategory: String) {
        viewModelScope.launch {
            _isLoading.value = true
            if (newCategory.trim().isNotEmpty()) {
                when (val result = repository.addNewCategory(newCategory)) {
                    is Result.Success<Category> -> {
                        _categories.value = listOf(result.data)
                    }
                    else -> {
                        _errorMessage.value = (result as Result.Error).exception.message
                    }
                }
            } else {
                _errorMessage.value = "category can't be empty text"
            }
            _isLoading.value = false
        }
    }

    fun addNewNote(selectedCategorySpinnerId: Long, selectedPrioritySpinnerId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            if (validInput(selectedCategorySpinnerId, selectedPrioritySpinnerId)) {
                val result: Result<String>
                if (noteId != -1L)
                    result =
                        repository.updateNote(
                            noteId,
                            titleText.value!!,
                            descText.value!!,
                            toEpoch(dateText.value!!),
                            selectedCategorySpinnerId,
                            selectedPrioritySpinnerId
                        )
                else
                    result = repository.addNewNote(
                        titleText.value!!,
                        descText.value!!,
                        toEpoch(dateText.value!!),
                        selectedCategorySpinnerId,
                        selectedPrioritySpinnerId,
                        System.currentTimeMillis()
                    )
                when (result) {
                    is Result.Success<String> -> {
                        _snackBarMessage.value = result.data!!
                        delay(1500)
                        _success.value = true
                    }
                    else -> {
                        _errorMessage.value = (result as Result.Error).exception.message
                    }
                }
            }
            _isLoading.value = false
        }
    }

    private fun validInput(
        selectedCategorySpinnerId: Long,
        selectedPrioritySpinnerId: Long
    ): Boolean {
        var result = true;
        if (dateText.value?.trim().isNullOrEmpty() ||
            try {
                toEpoch(dateText.value!!); false
            } catch (e: java.lang.Exception) {
                true
            }
        ) {
            _dateErrorMessage.value = "invalid date"
            result = false;

        } else {
            _dateErrorMessage.value = ""
        }

        if (titleText.value?.trim().isNullOrEmpty()) {
            _titleErrorMessage.value = "can't be empty"
            result = false;
        } else {
            _titleErrorMessage.value = ""
        }
        if (descText.value?.trim().isNullOrEmpty()) {
            _descErrorMessage.value = "can't be empty"
            result = false;
        } else {
            _descErrorMessage.value = ""
        }

        if (selectedCategorySpinnerId == -1L) {
            result = false
            _errorMessage.value = "choose category please"
        }
        if (selectedPrioritySpinnerId == -1L) {
            result = false
            _errorMessage.value = "choose priority please"
        }
        return result;
    }

    fun toEpoch(date: String): Long {
        return SimpleDateFormat("dd.MM.yyyy").parse(date).time / 1000L

    }

}