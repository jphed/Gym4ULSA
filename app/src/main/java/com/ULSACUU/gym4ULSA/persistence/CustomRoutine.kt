package com.ULSACUU.gym4ULSA.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "custom_routines")
@TypeConverters(ExerciseIdsConverter::class)
data class CustomRoutine(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val exerciseIds: List<Int>, // Guarda los IDs de los ejercicios
    val exerciseCount: Int
)