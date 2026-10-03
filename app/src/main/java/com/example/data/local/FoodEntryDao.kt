package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodEntryDao {
    @Query("SELECT * FROM food_entries WHERE dateString = :dateString ORDER BY timestampMillis ASC")
    fun getEntriesForDate(dateString: String): Flow<List<FoodEntryEntity>>

    @Query("SELECT * FROM food_entries WHERE dateString = :dateString AND hourOfDay = :hour ORDER BY timestampMillis ASC")
    fun getEntriesForDateAndHour(dateString: String, hour: Int): Flow<List<FoodEntryEntity>>

    @Query("SELECT * FROM food_entries ORDER BY timestampMillis DESC LIMIT :limit")
    fun getRecentEntries(limit: Int = 10): Flow<List<FoodEntryEntity>>

    @Query("SELECT * FROM food_entries ORDER BY timestampMillis DESC")
    fun getAllEntries(): Flow<List<FoodEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: FoodEntryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntries(entries: List<FoodEntryEntity>)

    @Delete
    suspend fun deleteEntry(entry: FoodEntryEntity)

    @Query("DELETE FROM food_entries WHERE id = :id")
    suspend fun deleteEntryById(id: Long)

    @Query("SELECT DISTINCT dateString FROM food_entries ORDER BY dateString DESC LIMIT 30")
    fun getLoggedDates(): Flow<List<String>>
}
