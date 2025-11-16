package com.ULSACUU.gym4ULSA.home

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ULSACUU.gym4ULSA.persistence.CustomRoutine
import com.ULSACUU.gym4ULSA.persistence.CustomRoutineRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

// Cambia a AndroidViewModel para tener el Contexto
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // Repositorios
    private val gistRepository = RoutinesRepository
    private val customRoutineRepository =
        CustomRoutineRepository(application) //  Instancia el repo de Room

    // Estados del Gist
    var gistRoutines by mutableStateOf<List<Routine>>(emptyList())
        private set
    var exercisesForSelectedGistRoutine by mutableStateOf<List<Exercise>>(emptyList())
        private set
    var selectedGistRoutine by mutableStateOf<Routine?>(null)
        private set

    // Estados de Rutinas Personalizadas (Room)
    var customRoutines by mutableStateOf<List<CustomRoutine>>(emptyList())
        private set
    var exercisesForSelectedCustomRoutine by mutableStateOf<List<Exercise>>(emptyList())
        private set
    var selectedCustomRoutine by mutableStateOf<CustomRoutine?>(null)
        private set

    //Estado de Todos los Ejercicios
    var allExercises by mutableStateOf<List<Exercise>>(emptyList())
        private set
    init {
        // Carga too al iniciar
        loadAllData()
    }

    private fun loadAllData() {
        viewModelScope.launch {
            try {
                // Carga los datos del Gist (rutinas y ejercicios)
                val response = gistRepository.fetchRoutines()
                allExercises = response.ejercicios
                gistRoutines = response.rutinas

                // Selecciona la primera rutina del Gist por defecto
                selectGistRoutine(gistRoutines.firstOrNull())

                // Escucha los cambios de las rutinas personalizadas (Flow)
                customRoutineRepository.allCustomRoutines.collect { routinesFromDb ->
                    customRoutines = routinesFromDb
                }

            } catch (e: Exception) {
                e.printStackTrace()
                // Maneja el error
            }
        }
    }

    //Función para seleccionar una rutina del GIST
    fun selectGistRoutine(routine: Routine?) {
        selectedGistRoutine = routine
        selectedCustomRoutine = null

        // Actualiza la lista de ejercicios
        exercisesForSelectedGistRoutine = routine?.let {
            allExercises.filter { ex -> ex.categoria.equals(it.musculo, ignoreCase = true) }
        } ?: emptyList()

        // Limpia la otra lista
        exercisesForSelectedCustomRoutine = emptyList()
    }

    // Función para seleccionar una rutina PERSONALIZADA
    fun selectCustomRoutine(routine: CustomRoutine?) {
        selectedCustomRoutine = routine
        selectedGistRoutine = null

        // Actualiza la lista de ejercicios filtrando por los IDs guardados
        exercisesForSelectedCustomRoutine = routine?.let {
            allExercises.filter { ex -> ex.id in it.exerciseIds }
        } ?: emptyList()

        // Limpiamos la otra lista
        exercisesForSelectedGistRoutine = emptyList()
    }

    //fun selectExercise(exercise: Exercise) {
    //    selectedExercise = exercise
   // }
}
