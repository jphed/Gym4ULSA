package com.ULSACUU.gym4ULSA.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ULSACUU.gym4ULSA.login.model.repository.AuthRepository
import com.ULSACUU.gym4ULSA.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/*
 LoginViewModel

 Este ViewModel administra el estado y la lógica del login.
 Es el puente entre la vista (Compose) y el repositorio (AuthRepository).

 Responsabilidades:
 - Mantener el estado de la UI (email, password, isLoading).
 - Ejecutar la acción de login usando coroutines.
 - Emitir mensajes de Toast/Eventos one-shot hacia la vista.
*/
class LoginViewModel(private val repo: AuthRepository) : ViewModel() {

    /*
     _ui: MutableStateFlow<LoginUiState>
     - Guarda el estado actual de la pantalla de login.
     - Es privado y mutable dentro del ViewModel.
     ui: StateFlow<LoginUiState>
     - Exposición pública inmutable para que la vista
       se suscriba y se redibuje cuando cambie el estado.
    */
    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui

    /*
     _toastEvents: Channel<ToastMessage>
     - Canal para enviar mensajes de Toast como recursos i18n.
     - BUFFERED: permite almacenar mensajes sin perderlos.
     toastEvents: Flow<ToastMessage>
     - Exposición como Flow, para que la vista los consuma
       de manera reactiva con collectLatest.
    */
    data class ToastMessage(val resId: Int, val args: List<String> = emptyList())
    private val _toastEvents = Channel<ToastMessage>(Channel.BUFFERED)
    val toastEvents = _toastEvents.receiveAsFlow()

    sealed interface LoginNavEvent {
        data object GoHome : LoginNavEvent
    }

    private val _navEvents = Channel<LoginNavEvent>(Channel.BUFFERED)
    val navEvent = _navEvents.receiveAsFlow()

    /*
     onEmailChange / onPasswordChange
     - Se llaman desde la vista cada vez que el usuario escribe.
     - Actualizan el estado de la UI con el nuevo valor.
    */
    fun onEmailChange(v: String) {
        _ui.value = _ui.value.copy(email = v)
    }

    fun onPasswordChange(v: String) {
        _ui.value = _ui.value.copy(password = v)
    }

    /*
     login()
     - Valida que email y password no estén vacíos.
     - Cambia el estado a isLoading = true mientras espera la respuesta.
     - Llama al repositorio para hacer la petición de login.
     - Si el login es exitoso:
       * Guarda la respuesta con token y usuario en el estado
       * Muestra "Login exitoso. Bienvenido"
       * Navega a la pantalla principal
     - Si ocurre una excepción:
       * Parsea el mensaje de error del servidor
       * Muestra el mensaje apropiado según el tipo de error
     - Al terminar (éxito o error) → regresa isLoading = false.
    */
    fun login() {
        val email = _ui.value.email.trim()
        val password = _ui.value.password
        if (email.isBlank() || password.isBlank()) {
            viewModelScope.launch { _toastEvents.send(ToastMessage(R.string.login_required_fields)) }
            return
        }
        _ui.value = _ui.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val response = repo.login(email, password)
                _ui.value = _ui.value.copy(currentUser = response)
                _toastEvents.send(ToastMessage(R.string.login_success_welcome, listOf("Usuario")))
                delay(500)
                _navEvents.send(LoginNavEvent.GoHome)
            } catch (e: Exception) {
                val msg = e.message?.lowercase() ?: ""
                val resId = when {
                    "correo" in msg || "email" in msg -> R.string.login_error_wrong_email
                    "contraseña" in msg || "password" in msg -> R.string.login_error_wrong_password
                    "red" in msg || "conexión" in msg -> R.string.network_error
                    else -> R.string.login_failed
                }
                _toastEvents.send(ToastMessage(resId))
            } finally {
                _ui.value = _ui.value.copy(isLoading = false)
            }
        }
    }
}