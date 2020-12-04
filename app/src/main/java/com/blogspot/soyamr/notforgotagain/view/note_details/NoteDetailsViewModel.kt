package com.blogspot.soyamr.notforgotagain.view.note_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.model.db.tables.FullNoteData
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class NoteDetailsViewModelFactory(
    private val repository: NoteRepository,
    private val noteId: Long
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        NoteDetailsViewModel(repository, noteId) as T
}

class NoteDetailsViewModel(private val repository: NoteRepository, private val noteId: Long) :
    ViewModel() {
    val note = MutableLiveData<FullNoteData>()
    var date = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            note.value = repository.getFullNoteData(noteId)
            date.value = makeDateString()
        }
    }

    private fun makeDateString(): String {
        var formattedDate: String
        try {
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            val netDate = Date(note.value?.nDeadline!! * 1000)
            formattedDate = sdf.format(netDate)
            val deadline = sdf.parse(formattedDate)
            val current = sdf.parse(sdf.format(Calendar.getInstance().time))
            if (deadline!!.after(current))
                return "До $formattedDate"
            return "Был в $formattedDate"


        } catch (e: Exception) {
            return e.toString()
        }
    }

}