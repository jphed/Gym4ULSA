package com.ULSACUU.gym4ULSA.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomRoutineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(routine: CustomRoutine)

    @Query("SELECT * FROM custom_routines ORDER BY id DESC")
    fun getAll(): Flow<List<CustomRoutine>>

    @Query("DELETE FROM custom_routines WHERE id = :id")
    suspend fun delete(id: Int)
}