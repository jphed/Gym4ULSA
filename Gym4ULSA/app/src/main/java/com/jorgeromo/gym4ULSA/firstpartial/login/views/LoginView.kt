package com.jorgeromo.gym4ULSA.firstpartial.login.views

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jorgeromo.gym4ULSA.R
import com.jorgeromo.gym4ULSA.firstpartial.login.model.network.RetrofitProvider
import com.jorgeromo.gym4ULSA.firstpartial.login.model.repository.AuthRepository
import com.jorgeromo.gym4ULSA.firstpartial.login.viewmodel.LoginViewModel
import com.jorgeromo.gym4ULSA.firstpartial.login.viewmodel.LoginViewModelFactory
import com.jorgeromo.gym4ULSA.navigation.ScreenNavigation
import com.jorgeromo.gym4ULSA.ui.theme.OrangeRed
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

    // UI del login con fondo animado
    Scaffold(containerColor = Color.Transparent) { padding ->
        // Layer 1: background should fill the entire screen behind the TopAppBar
        Box(modifier = Modifier.fillMaxSize()) {
            MinimalAnimatedBackground()

            // Layer 2: foreground content respects the inner padding from Scaffold
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(padding)
            ) {
                // Foreground content card
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(46.dp),
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.Center)
                        .shadow(
                            elevation = 8.dp,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(46.dp),
                            clip = false
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // App brand
                        Image(
                            painter = painterResource(id = R.drawable.applogo),
                            contentDescription = "ULSA logo",
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "Welcome back",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
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
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
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
        }
    }
}

@Composable
private fun MinimalAnimatedBackground() {
    val red = MaterialTheme.colorScheme.primary
    val white = Color.White
    val bg = MaterialTheme.colorScheme.background.takeIf { it != Color.Unspecified } ?: Color(0xFF0A0A0A)

    // Minimalistic: solid background with subtle diagonal moving lines (no gradients)
    val transition = rememberInfiniteTransition(label = "minimal_lines")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "phase"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        // Solid background
        drawRect(color = bg)

        // Draw thin diagonal lines, seamlessly scrolling
        val angleDeg = -30f
        val spacing = (size.minDimension / 22f).coerceIn(12f, 36f)
        val thickness = (spacing * 0.08f).coerceIn(1.2f, 3.2f)
        val travel = spacing
        val offset = phase * travel

        // Rotate canvas so we can draw horizontal lines that appear diagonal
        rotate(degrees = angleDeg) {
            // After rotation, the needed width/height expand; draw across extended bounds
            val w = size.width * 2f
            val h = size.height * 2f
            // Start drawing lines above the visible area to ensure full coverage
            var y = -h + offset
            var i = 0
            while (y < h) {
                val isRed = (i % 3) != 0 // 2 red lines, then 1 white line for variety
                val color = if (isRed) red.copy(alpha = 0.10f) else white.copy(alpha = 0.06f)
                drawRect(
                    color = color,
                    topLeft = Offset(-w / 2f, y),
                    size = androidx.compose.ui.geometry.Size(w, thickness)
                )
                y += spacing
                i++
            }
        }
    }
}
