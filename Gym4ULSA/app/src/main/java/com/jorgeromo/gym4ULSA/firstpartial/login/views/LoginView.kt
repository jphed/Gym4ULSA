package com.jorgeromo.gym4ULSA.ids.login.views

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jorgeromo.gym4ULSA.R
import com.jorgeromo.gym4ULSA.firstpartial.login.model.network.RetrofitProvider
import com.jorgeromo.gym4ULSA.firstpartial.login.model.repository.AuthRepository
import com.jorgeromo.gym4ULSA.firstpartial.login.viewmodel.LoginViewModel
import com.jorgeromo.gym4ULSA.firstpartial.login.viewmodel.LoginViewModelFactory
import com.jorgeromo.gym4ULSA.navigation.ScreenNavigation
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginView(navController: NavController) {
    // Repositorio y ViewModel
    val repo = remember { AuthRepository(RetrofitProvider.authApi) }
    val vm: LoginViewModel = viewModel(factory = LoginViewModelFactory(repo))
    val ui by vm.ui.collectAsState()

    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }

    // Función para mostrar Toast seguro
    fun showToastSafe(text: String) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Escucha los mensajes de toast
    LaunchedEffect(vm) {
        vm.toastEvents.collectLatest { msg ->
            showToastSafe(msg)
        }
    }

    // Escucha eventos de navegación
    LaunchedEffect(vm) {
        vm.navEvent.collectLatest { event ->
            when(event) {
                is LoginViewModel.LoginNavEvent.GoHome -> {
                    // Navega a "Rutina" después del login exitoso
                    navController.navigate(ScreenNavigation.FirstPartial.route) {
                        popUpTo(ScreenNavigation.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    // UI del login
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ulsalogo),
                contentDescription = "ULSA logo",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.height(16.dp))

            // Email
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

            // Password
            OutlinedTextField(
                value = ui.password,
                onValueChange = vm::onPasswordChange,
                label = { Text(stringResource(R.string.password_label)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { vm.login() }),
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

            // Botón login
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

            // Botón Face ID
            OutlinedButton(
                onClick = { /* TODO: BiometricPrompt flow */ },
                enabled = !ui.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Face, contentDescription = "Face ID", modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Face ID")
            }
        }
    }
}
