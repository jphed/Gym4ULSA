package com.jorgeromo.androidClassMp1.firstpartial.Login.views

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jorgeromo.androidClassMp1.R
import com.jorgeromo.androidClassMp1.firstpartial.login.model.network.RetrofitProvider
import com.jorgeromo.androidClassMp1.firstpartial.login.model.repository.AuthRepository
import com.jorgeromo.androidClassMp1.firstpartial.login.viewmodel.LoginViewModel
import com.jorgeromo.androidClassMp1.firstpartial.login.viewmodel.LoginViewModelFactory
import com.jorgeromo.androidClassMp1.ui.theme.AndroidClassMP1Theme
import kotlinx.coroutines.flow.collectLatest

/*
 LoginView

 Pantalla de UI construida con Jetpack Compose que muestra
 el formulario de inicio de sesión.

 Responsabilidades:
 - Mostrar los campos de email y password.
 - Mostrar botones de login y Face ID.
 - Escuchar cambios en el estado (LoginUiState) y redibujar la UI.
 - Mostrar mensajes de Toast y Snackbar basados en eventos
   emitidos por el ViewModel.
*/
@Composable
fun LoginView() {
    // Inyección simple del repositorio y el ViewModel
    val repo = remember { AuthRepository(RetrofitProvider.authApi) }
    val vm: LoginViewModel = viewModel(factory = LoginViewModelFactory(repo))
    val ui by vm.ui.collectAsState()

    val appContext = LocalContext.current.applicationContext
    val snackbarHostState = remember { SnackbarHostState() }
    var passwordVisible by remember { mutableStateOf(false) }

    /*
     showToastSafe(text)
     Función auxiliar que asegura que el Toast
     siempre se ejecute en el hilo principal,
     evitando errores en algunos emuladores.
    */
    fun showToastSafe(text: String) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(appContext, text, Toast.LENGTH_SHORT).show()
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(appContext, text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*
     LaunchedEffect(vm)
     Escucha los eventos de toastEvents emitidos por el ViewModel.
     - Muestra un Toast con el mensaje recibido.
     - También muestra un Snackbar integrado a la UI.
    */
    LaunchedEffect(vm) {
        vm.toastEvents.collectLatest { msg ->
            showToastSafe(msg)
            snackbarHostState.showSnackbar(message = msg)
        }
    }

    /*
     Scaffold
     Contenedor de Material 3 que provee un layout base
     con soporte para SnackbarHost.
    */
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo institucional
            Image(
                painter = painterResource(id = R.drawable.ulsalogo),
                contentDescription = "ULSA logo",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.height(16.dp))

            // Campo de email
            OutlinedTextField(
                value = ui.email,
                onValueChange = vm::onEmailChange,
                label = { Text(stringResource(R.string.email_label)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // Campo de password con botón para mostrar/ocultar
            OutlinedTextField(
                value = ui.password,
                onValueChange = vm::onPasswordChange,
                label = { Text(stringResource(R.string.password_label)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { vm.login() }
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = "Toggle password visibility")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Botón de login
            Button(
                onClick = { vm.login() },
                enabled = !ui.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (ui.isLoading) {
                    CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Signing in…")
                } else {
                    Text(stringResource(R.string.login_button))
                }
            }

            Spacer(Modifier.height(12.dp))

            // Botón de Face ID (placeholder, aún no implementado)
            OutlinedButton(
                onClick = { /* TODO: BiometricPrompt flow */ },
                enabled = !ui.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "Face ID",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Face ID")
            }

            Spacer(Modifier.height(20.dp))

            // Botón para probar que los Toasts funcionan en emulador
            TextButton(onClick = { showToastSafe("Prueba de Toast 👋") }) {
                Text("Probar toast")
            }
        }
    }
}

/*
 LoginViewPreview

 Previsualización de la pantalla en Android Studio.
 No ejecuta lógica real de login, solo dibuja la UI.
*/
@Preview(showBackground = true)
@Composable
fun LoginViewPreview() {
    AndroidClassMP1Theme { LoginView() }
}