package com.jorgeromo.androidClassMp1.firstpartial.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgeromo.androidClassMp1.firstpartial.login.model.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
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
     _toastEvents: Channel<String>
     - Canal para enviar mensajes de texto (eventos de Toast).
     - BUFFERED: permite almacenar mensajes sin perderlos.
     toastEvents: Flow<String>
     - Exposición como Flow, para que la vista los consuma
       de manera reactiva con collectLatest.
    */
    private val _toastEvents = Channel<String>(Channel.BUFFERED)
    val toastEvents = _toastEvents.receiveAsFlow()


    sealed interface  LoginNavEvent {
        data object  GoHome : LoginNavEvent
    }

    private val _navEvents = Channel<LoginNavEvent>(Channel.BUFFERED)
    val navEvent = _navEvents.receiveAsFlow()
    /*
     onEmailChange / onPasswordChange
     - Se llaman desde la vista cada vez que el usuario escribe.
     - Actualizan el estado de la UI con el nuevo valor.
    */
    fun onEmailChange(v: String) { _ui.value = _ui.value.copy(email = v) }
    fun onPasswordChange(v: String) { _ui.value = _ui.value.copy(password = v) }

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
            viewModelScope.launch { _toastEvents.send("Email y password son obligatorios") }
            return
        }

        _ui.value = _ui.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val res = repo.login(email, password)
                if (res.success) {
                    _toastEvents.send("Login exitoso. Bienvenido ${res.user?.name ?: ""}")
                    _navEvents.send(LoginNavEvent.GoHome)
                } else {
                    _toastEvents.send(res.message.ifBlank { "Login fallido" })
                }
            } catch (e: Exception) {
                _toastEvents.send("Error de red/servidor")
            } finally {
                _ui.value = _ui.value.copy(isLoading = false)
            }
        }
    }
}