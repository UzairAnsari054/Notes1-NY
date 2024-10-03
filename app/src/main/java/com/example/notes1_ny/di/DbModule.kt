package com.example.notes1_ny.di

import android.content.Context
import androidx.room.Room
import com.example.notes1_ny.data.NoteDao
import com.example.notes1_ny.data.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(
        context = app,
        klass = NoteDatabase::class.java,
        name = "Notes_db"
    ).build()

    @Provides
    @Singleton
    fun provideNoteDao(noteDatabase: NoteDatabase): NoteDao {
        return noteDatabase.getNoteDao()
    }
}