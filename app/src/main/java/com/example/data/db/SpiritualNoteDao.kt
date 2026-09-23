package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.SpiritualNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SpiritualNoteDao {

    @Query("SELECT * FROM spiritual_notes ORDER BY isPinned DESC, timestamp DESC")
    fun getAllNotesFlow(): Flow<List<SpiritualNoteEntity>>

    @Query("SELECT * FROM spiritual_notes WHERE categoryCode = :categoryCode ORDER BY isPinned DESC, timestamp DESC")
    fun getNotesByCategoryFlow(categoryCode: String): Flow<List<SpiritualNoteEntity>>

    @Query("SELECT * FROM spiritual_notes ORDER BY isPinned DESC, timestamp DESC")
    suspend fun getAllNotesDirect(): List<SpiritualNoteEntity>

    @Query("SELECT * FROM spiritual_notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: Long): SpiritualNoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: SpiritualNoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<SpiritualNoteEntity>)

    @Update
    suspend fun updateNote(note: SpiritualNoteEntity)

    @Query("DELETE FROM spiritual_notes WHERE id = :id")
    suspend fun deleteNoteById(id: Long)

    @Query("DELETE FROM spiritual_notes WHERE categoryCode = 'CONFESSION' AND isCompletedOrConfessed = 1")
    suspend fun clearConfessedNotes()

    @Query("DELETE FROM spiritual_notes")
    suspend fun clearAllNotes()
}
