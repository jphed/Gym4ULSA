package com.ULSACUU.gym4ULSA.persistence

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ExerciseIdsConverter {
    @TypeConverter
    fun fromList(value: List<Int>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toList(value: String): List<Int> {
        return Json.decodeFromString(value)
    }
}