package com.ULSACUU.gym4ULSA.create_routine.viewmodel


import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ULSACUU.gym4ULSA.home.Exercise
import com.ULSACUU.gym4ULSA.home.RoutinesRepository
import com.ULSACUU.gym4ULSA.persistence.CustomRoutine
import com.ULSACUU.gym4ULSA.persistence.CustomRoutineRepository
import kotlinx.coroutines.launch

class CreateRoutineViewModel(application: Application) : AndroidViewModel(application) {

    //  Repositorios
    private val gistRepository = RoutinesRepository
    private val customRoutineRepository = CustomRoutineRepository(application)

    // Estado de la UI
    var routineName by mutableStateOf("Mi Rutina Personalizada")
    var allExercises by mutableStateOf<List<Exercise>>(emptyList())
    var addedExercises by mutableStateOf<List<Exercise>>(emptyList())

    //Estado para el Bottom Sheet
    var isSheetOpen by mutableStateOf(false)

    init {
        fetchAllExercises()
    }

    private fun fetchAllExercises() {
        viewModelScope.launch {
            try {
                // Obtener toodos los ejercicios disponibles del Gist
                allExercises = gistRepository.fetchRoutines().ejercicios
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addExercise(exercise: Exercise) {
        if (exercise !in addedExercises) {
            addedExercises = addedExercises + exercise
        }
        isSheetOpen = false // Cierra el sheet después de seleccionar
    }

    fun removeExercise(exercise: Exercise) {
        addedExercises = addedExercises - exercise
    }

    fun saveRoutine(onRoutineSaved: () -> Unit) {
        if (routineName.isBlank() || addedExercises.isEmpty()) {
            // (Opcional) Mostrar un error al usuario
            return
        }

        viewModelScope.launch {
            val newRoutine = CustomRoutine(
                name = routineName,
                exerciseIds = addedExercises.map { it.id },
                exerciseCount = addedExercises.size
            )
            customRoutineRepository.insert(newRoutine)

            // Llama al callback para navegar hacia atrás
            onRoutineSaved()
        }
    }
}