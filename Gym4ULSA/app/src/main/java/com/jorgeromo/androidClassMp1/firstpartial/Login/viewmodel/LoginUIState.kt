package com.jorgeromo.androidClassMp1.firstpartial.login.viewmodel

/*
 LoginUiState

 Esta clase representa el "estado de la UI" para la pantalla de login.
 Es un data class inmutable que guarda los valores necesarios para que
 la vista pueda dibujarse de manera reactiva.

 Campos:
 - email: el texto que el usuario escribe en el campo de correo.
 - password: el texto que el usuario escribe en el campo de contraseña.
 - isLoading: indica si la app está esperando la respuesta del servidor
   (true = se muestra un loading/spinner, false = se muestran los botones).

 Ventaja:
 Al ser un objeto inmutable, cada cambio genera una nueva copia con los
 valores actualizados, lo que facilita el manejo de estados en Compose.
*/
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false
)