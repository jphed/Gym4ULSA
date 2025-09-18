package com.jorgeromo.gym4ULSA.firstpartial.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgeromo.gym4ULSA.firstpartial.login.model.repository.AuthRepository
import com.jorgeromo.gym4ULSA.R
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
     - Según el resultado:
       * success = true → muestra "Login exitoso. Bienvenido <nombre>"
       * success = false → muestra el mensaje que viene del servidor
         o "Login fallido" si está vacío.
     - Si ocurre una excepción de red → muestra "Error de red/servidor".
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
                val res = repo.login(email, password)
                if (res.success) {
                    _toastEvents.send(ToastMessage(R.string.login_success_welcome, listOf(res.user?.name ?: "")))
                    delay(500)
                    _navEvents.send(LoginNavEvent.GoHome)
                } else {
                    val msg = res.message.lowercase()
                    val errorMsg = when {
                        "correo" in msg || "email" in msg -> null to R.string.login_error_wrong_email
                        "contraseña" in msg || "password" in msg -> null to R.string.login_error_wrong_password
                        else -> null to R.string.login_failed
                    }
                    _toastEvents.send(ToastMessage(errorMsg.second))
                }
            } catch (e: Exception) {
                _toastEvents.send(ToastMessage(R.string.network_error))
            } finally {
                _ui.value = _ui.value.copy(isLoading = false)
            }
        }
    }
}