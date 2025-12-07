package com.ULSACUU.gym4ULSA.sign_up.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ULSACUU.gym4ULSA.sign_up.model.Genero
import com.ULSACUU.gym4ULSA.sign_up.model.NivelExperiencia
import com.ULSACUU.gym4ULSA.sign_up.model.ObjetivoFitness
import com.ULSACUU.gym4ULSA.sign_up.model.RegistroState
import com.ULSACUU.gym4ULSA.login.model.repository.AuthRepository
import com.ULSACUU.gym4ULSA.login.model.dto.Goal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

class RegistroViewModel(
    private val repository: AuthRepository = AuthRepository(
        com.ULSACUU.gym4ULSA.login.model.network.RetrofitProvider.authApi
    )
) : ViewModel() {

    private val _state = MutableStateFlow(RegistroState())
    val state: StateFlow<RegistroState> = _state.asStateFlow()

    fun onEvent(event: RegistroEvent) {
        when(event) {
            is RegistroEvent.EnteredName -> _state.update { it.copy(name = event.value) }
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
        if (s.name.isBlank() || s.email.isBlank() || s.password.isBlank() || s.edad.isBlank() || !s.terminosAceptados) {
            _state.update { it.copy(error = "Por favor completa todos los campos obligatorios.") }
            return
        }

        // Validaciones de datos numéricos
        val edadNum = s.edad.toIntOrNull()
        val alturaNum = s.altura.toIntOrNull()
        val pesoNum = s.pesoActual.toIntOrNull()
        val metaNum = s.metaPeso.toIntOrNull()

        if (edadNum == null || edadNum < 10 || edadNum > 120) {
            _state.update { it.copy(error = "Por favor ingresa una edad válida (10-120 años).") }
            return
        }

        if (alturaNum == null || alturaNum < 100 || alturaNum > 250) {
            _state.update { it.copy(error = "Por favor ingresa una altura válida (100-250 cm).") }
            return
        }

        if (pesoNum == null || pesoNum < 30 || pesoNum > 300) {
            _state.update { it.copy(error = "Por favor ingresa un peso válido (30-300 kg).") }
            return
        }

        if (metaNum == null || metaNum < 30 || metaNum > 300) {
            _state.update { it.copy(error = "Por favor ingresa un peso meta válido (30-300 kg).") }
            return
        }

        if (s.genero == null) {
            _state.update { it.copy(error = "Por favor selecciona tu género.") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, successMessage = null) }
            try {
                // Convertir datos del formulario al formato del backend
                val sex = when (s.genero) {
                    Genero.MASCULINO -> "male"
                    Genero.FEMENINO -> "female"
                    Genero.OTRO -> "other"
                    else -> "other"
                }

                val goalType = when (s.objetivoFitness) {
                    ObjetivoFitness.DEFICIT -> "deficit"
                    ObjetivoFitness.VOLUMEN -> "volumen"
                    else -> "deficit"
                }

                val goal = Goal(
                    type = goalType,
                    target_weight_kg = metaNum?.toString() ?: "0",
                    rate_per_week_kg = "0.5"
                )

                val experiencia = when (s.nivelExperiencia) {
                    NivelExperiencia.PRINCIPIANTE -> "principiante"
                    NivelExperiencia.INTERMEDIO -> "intermedio"
                    NivelExperiencia.AVANZADO -> "avanzado"
                }

                val response = repository.signup(
                    name = s.name, // Usar el nombre del formulario
                    email = s.email,
                    password = s.password,
                    age = edadNum.toString(),
                    sex = sex,
                    height_cm = alturaNum.toString(),
                    weight_kg = pesoNum.toString(),
                    goal = goal,
                    activity_level = "",
                    experiencia = experiencia,
                    created_utc = Instant.now().toString()
                )

                // Registro exitoso
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        successMessage = "Usuario creado exitosamente"
                    )
                }
                
                // TODO: Navegar a la pantalla principal
                println("Usuario registrado exitosamente: ${response.user.id}")
                
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, successMessage = null) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}

// Eventos para desacoplar la UI de la lógica
sealed class RegistroEvent {
    data class EnteredName(val value: String): RegistroEvent()
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