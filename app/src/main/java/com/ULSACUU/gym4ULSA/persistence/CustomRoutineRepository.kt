package com.ULSACUU.gym4ULSA.persistence

import android.content.Context
import kotlinx.coroutines.flow.Flow

class CustomRoutineRepository(context: Context) {
    private val dao = AppDatabase.getDatabase(context).customRoutineDao()

    val allCustomRoutines: Flow<List<CustomRoutine>> = dao.getAll()

    suspend fun insert(routine: CustomRoutine) {
        dao.insert(routine)
    }
}