package com.ULSACUU.gym4ULSA.home

import com.ULSACUU.gym4ULSA.home.Exercise
import com.ULSACUU.gym4ULSA.home.Routine
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

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
        fetchRoutines()
    }

    private fun fetchRoutines() {
        viewModelScope.launch {
            try {
                val response = RoutinesRepository.fetchRoutines()

                routines = response.rutinas
                // Guardamos todos los ejercicios para filtrar después
                setAllExercises(response.ejercicios)

                selectedRoutine = routines.firstOrNull()
                updateExercisesForSelectedRoutine()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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

    private fun updateExercisesForSelectedRoutine() {
        exercisesForSelectedRoutine = selectedRoutine?.let { routine ->
            routinesExercises.filter { it.categoria.equals(routine.musculo, ignoreCase = true) }
        } ?: emptyList()

        selectedExercise = exercisesForSelectedRoutine.firstOrNull()
    }

    fun selectRoutine(routine: Routine) {
        selectedRoutine = routine
        updateExercisesForSelectedRoutine()
    }

    fun selectExercise(exercise: Exercise) {
        selectedExercise = exercise
    }
}
