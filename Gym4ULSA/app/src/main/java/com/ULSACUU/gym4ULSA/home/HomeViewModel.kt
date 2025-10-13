package com.ULSACUU.gym4ULSA.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URL

class HomeViewModel : ViewModel() {

    var routines by mutableStateOf<List<Routine>>(emptyList())
        private set

    var selectedRoutine by mutableStateOf<Routine?>(null)
        private set

    var selectedExercise by mutableStateOf<Exercise?>(null)
        private set

    var exercisesForSelectedRoutine by mutableStateOf<List<Exercise>>(emptyList())
        private set

    init {
        fetchGistData()
    }

    private fun fetchGistData() {
        viewModelScope.launch {
            try {
                val jsonString = withContext(Dispatchers.IO) {
                    URL("https://gist.githubusercontent.com/YajahiraPP/5d64c24ac355f7c309eeb7d3cdc614b3/raw/13db2f8550ce2f20961f3289d5b0c8d7a5169fef/routines.json")
                        .readText()
                }

                val data = Json { ignoreUnknownKeys = true }
                    .decodeFromString<RoutinesResponse>(jsonString)

                routines = data.rutinas
                selectedRoutine = routines.firstOrNull()
                updateExercisesForSelectedRoutine()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateExercisesForSelectedRoutine() {
        exercisesForSelectedRoutine = selectedRoutine?.let { routine ->
            // Filtrar ejercicios según la categoría (musculo de la rutina)
            routinesExercises.filter { it.categoria.equals(routine.musculo, ignoreCase = true) }
        } ?: emptyList()

        selectedExercise = exercisesForSelectedRoutine.firstOrNull()
    }

    // Guardar todos los ejercicios para filtrarlos
    private var routinesExercises: List<Exercise> = emptyList()
        set(value) {
            field = value
            updateExercisesForSelectedRoutine()
        }

    fun setAllExercises(exercises: List<Exercise>) {
        routinesExercises = exercises
    }

    fun selectRoutine(routine: Routine) {
        selectedRoutine = routine
        updateExercisesForSelectedRoutine()
    }

    fun selectExercise(exercise: Exercise) {
        selectedExercise = exercise
    }
}
