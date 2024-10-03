package com.example.notes1_ny.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note_table ORDER BY createdDate")
    fun getAllNotesOrderedByDate(): Flow<List<Note>>

    @Query("SELECT * FROM note_table ORDER BY title ASC")
    fun getAllNotesOrderByTitle(): Flow<List<Note>>
}