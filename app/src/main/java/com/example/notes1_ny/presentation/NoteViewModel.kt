package com.example.notes1_ny.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes1_ny.data.Note
import com.example.notes1_ny.data.NoteDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteDao: NoteDao
) : ViewModel() {

    private val isSortedByDate: MutableStateFlow<Boolean> = MutableStateFlow(true)

    val noteList = isSortedByDate.flatMapLatest { isSortedByDate ->
        if (isSortedByDate) {
            noteDao.getAllNotesOrderedByDate()
        } else {
            noteDao.getAllNotesOrderByTitle()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    val _state = MutableStateFlow(NoteState())
    val state = combine(_state, isSortedByDate, noteList) { state, isSortedByDate, noteList ->
        state.copy(noteList = noteList)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())

    fun onEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    noteDao.deleteNote(event.note)
                }
            }

            is NoteEvent.SaveNote -> {
                val note = Note(
                    title = state.value.title.value,
                    description = state.value.description.value,
                    createdDate = System.currentTimeMillis()
                )
                viewModelScope.launch(Dispatchers.IO) {
                    noteDao.upsertNote(note)
                }

                _state.update {
                    it.copy(
                        title = mutableStateOf(""),
                        description = mutableStateOf("")
                    )
                }
            }

            NoteEvent.SortNotes -> {
                isSortedByDate.value = !isSortedByDate.value
            }
        }
    }
}