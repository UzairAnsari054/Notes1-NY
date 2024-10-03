package com.example.notes1_ny.presentation

import com.example.notes1_ny.data.Note

sealed interface NoteEvent {
    object SortNotes : NoteEvent
    data class SaveNote(val title: String, val description: String) : NoteEvent
    data class DeleteNote(val note: Note) : NoteEvent
}