package com.ULSACUU.gym4ULSA.sign_up.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ULSACUU.gym4ULSA.sign_up.model.Genero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.ULSACUU.gym4ULSA.sign_up.model.RegistroState
import com.ULSACUU.gym4ULSA.sign_up.model.NivelExperiencia
import com.ULSACUU.gym4ULSA.sign_up.model.ObjetivoFitness

class RegistroViewModel : ViewModel() {

    private val _state = MutableStateFlow(RegistroState())
    val state: StateFlow<RegistroState> = _state.asStateFlow()

    fun onEvent(event: RegistroEvent) {
        when(event) {
            is RegistroEvent.EnteredEmail -> _state.update { it.copy(email = event.value) }
            is RegistroEvent.EnteredPassword -> _state.update { it.copy(password = event.value) }
            is RegistroEvent.EnteredEdad -> _state.update { it.copy(edad = event.value) }
            is RegistroEvent.EnteredPeso -> _state.update { it.copy(pesoActual = event.value) }
            is RegistroEvent.EnteredAltura -> _state.update { it.copy(altura = event.value) }
            is RegistroEvent.EnteredMeta -> _state.update { it.copy(metaPeso = event.value) }
            is RegistroEvent.SelectedExperiencia -> _state.update { it.copy(nivelExperiencia = event.value) }
            is RegistroEvent.SelectedObjetivo -> _state.update { it.copy(objetivoFitness = event.value) }
            is RegistroEvent.SelectedGenero -> _state.update { it.copy(genero = event.value) }
            is RegistroEvent.ToggleTerminos -> _state.update { it.copy(terminosAceptados = event.value) }
            RegistroEvent.Submit -> registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        // Validaciones básicas antes de enviar
        val s = _state.value
        if (s.email.isBlank() || s.password.isBlank() || s.edad.isBlank() || !s.terminosAceptados) {
            _state.update { it.copy(error = "Por favor completa todos los campos obligatorios.") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                // val response = repository.register(s.toDto())

                // Simulación de éxito:
                println("Usuario registrado: ${s.email}, Peso: ${s.pesoActual}")
                // Navegar a la Home
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}

// Eventos para desacoplar la UI de la lógica
sealed class RegistroEvent {
    data class EnteredEmail(val value: String): RegistroEvent()
    data class EnteredPassword(val value: String): RegistroEvent()
    data class EnteredEdad(val value: String): RegistroEvent()
    data class EnteredPeso(val value: String): RegistroEvent()
    data class EnteredAltura(val value: String): RegistroEvent()
    data class EnteredMeta(val value: String): RegistroEvent()
    data class SelectedExperiencia(val value: NivelExperiencia): RegistroEvent()
    data class SelectedObjetivo(val value: ObjetivoFitness): RegistroEvent()
    data class SelectedGenero(val value: Genero): RegistroEvent()
    data class ToggleTerminos(val value: Boolean): RegistroEvent()
    object Submit: RegistroEvent()
}