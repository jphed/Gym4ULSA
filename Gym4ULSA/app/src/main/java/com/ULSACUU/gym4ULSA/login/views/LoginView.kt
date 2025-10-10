package com.ULSACUU.gym4ULSA.login.views

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
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.animation.core.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ULSACUU.gym4ULSA.R
import com.ULSACUU.gym4ULSA.login.model.network.RetrofitProvider
import com.ULSACUU.gym4ULSA.login.model.repository.AuthRepository
import com.ULSACUU.gym4ULSA.login.viewmodel.LoginViewModel
import com.ULSACUU.gym4ULSA.login.viewmodel.LoginViewModelFactory
import com.ULSACUU.gym4ULSA.navigation.ScreenNavigation
import kotlinx.coroutines.flow.collectLatest
import com.ULSACUU.gym4ULSA.utils.DataStoreManager
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.ULSACUU.gym4ULSA.utils.CredentialsStore
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

private enum class BiometricModality { FACE, FINGERPRINT, NONE }

private fun detectBiometricModality(context: Context): BiometricModality {
    val bm = BiometricManager.from(context)
    val can = bm.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
    if (can != BiometricManager.BIOMETRIC_SUCCESS) return BiometricModality.NONE

    val pm = context.packageManager
    val hasFace = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        pm.hasSystemFeature(PackageManager.FEATURE_FACE)
    } else {
        false
    }
    val hasFp = pm.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    return when {
        hasFace -> BiometricModality.FACE
        hasFp -> BiometricModality.FINGERPRINT
        else -> BiometricModality.FINGERPRINT // default to fingerprint icon/text if unknown
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(navController: NavController) {
    // Repositorio y ViewModel
    val repo = remember { AuthRepository(RetrofitProvider.authApi) }
    val vm: LoginViewModel = viewModel(factory = LoginViewModelFactory(repo))
    val ui by vm.ui.collectAsState()

    val context = LocalContext.current
    val ds = remember { DataStoreManager(context) }
    val credsStore = remember { CredentialsStore(context) }
    val scope = rememberCoroutineScope()
    val rememberCreds by ds.rememberCredentialsFlow.collectAsState(initial = false)
    val savedEmail by ds.savedEmailFlow.collectAsState(initial = "")
    val promptedEmails by ds.promptedEmailsFlow.collectAsState(initial = emptySet())
    var passwordVisible by remember { mutableStateOf(false) }
    var showRememberDialog by remember { mutableStateOf(false) }

    // Biometric capability and prompt setup
    val modality by remember(context) { mutableStateOf(detectBiometricModality(context)) }
    val biometricAvailable = modality != BiometricModality.NONE
    val activity = remember(context) { context.findActivity() }
    val executor = remember(context) { ContextCompat.getMainExecutor(context) }
    val biometricPrompt = remember(activity, executor) {
        val fa = activity as? FragmentActivity
        if (fa == null) null else BiometricPrompt(
            fa,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Navigate directly to Home on success
                    navController.navigate(ScreenNavigation.Home.route) {
                        popUpTo(ScreenNavigation.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        )
    }
    val promptInfo = remember(modality) {
        val title = when (modality) {
            BiometricModality.FACE -> "Unlock with Face"
            BiometricModality.FINGERPRINT -> "Unlock with Fingerprint"
            else -> "Biometric unlock"
        }
        BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle("Quickly sign in")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .setNegativeButtonText("Cancel")
            .build()
    }

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

    // Escucha los mensajes de toast (i18n via resource IDs)
    LaunchedEffect(vm) {
        vm.toastEvents.collectLatest { tm ->
            val text = if (tm.args.isEmpty()) {
                context.getString(tm.resId)
            } else {
                context.getString(tm.resId, *tm.args.toTypedArray())
            }
            showToastSafe(text)
        }
    }

    // Prefill saved email and password if user opted to remember and fields are empty
    LaunchedEffect(rememberCreds, savedEmail) {
        if (rememberCreds && savedEmail.isNotBlank()) {
            if (ui.email.isBlank()) vm.onEmailChange(savedEmail)
            if (ui.password.isBlank()) {
                credsStore.getPassword(savedEmail)?.let { storedPwd ->
                    vm.onPasswordChange(storedPwd)
                }
            }
        }
    }

    // Escucha eventos de navegación
    LaunchedEffect(vm) {
        vm.navEvent.collectLatest { event ->
            when(event) {
                is LoginViewModel.LoginNavEvent.GoHome -> {
                    val emailNow = ui.email
                    val wasPrompted = promptedEmails.contains(emailNow)
                    if (!wasPrompted) {
                        // Show dialog only the first time we see this email
                        showRememberDialog = true
                    } else {
                        // Already prompted before: just navigate to Home
                        navController.navigate(ScreenNavigation.Home.route) {
                            popUpTo(ScreenNavigation.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    }

    // UI del login con fondo animado
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { }
                // 👆 sin navigationIcon, queda limpio
            )
        }
    ) { padding ->
        if (showRememberDialog) {
            AlertDialog(
                onDismissRequest = {
                    // If dismissed, assume no and continue
                    scope.launch {
                        ds.setRememberCredentials(false)
                        ds.setSavedEmail("")
                        ds.markEmailPrompted(ui.email)
                        credsStore.clearCredentials(ui.email)
                    }
                    showRememberDialog = false
                    navController.navigate(ScreenNavigation.Home.route) {
                        popUpTo(ScreenNavigation.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        scope.launch {
                            ds.setRememberCredentials(true)
                            ds.setSavedEmail(ui.email)
                            ds.markEmailPrompted(ui.email)
                            credsStore.setCredentials(ui.email, ui.password)
                        }
                        showRememberDialog = false
                        navController.navigate(ScreenNavigation.Home.route) {
                            popUpTo(ScreenNavigation.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }) {
                        Text(text = stringResource(id = R.string.remember_yes))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        scope.launch {
                            ds.setRememberCredentials(false)
                            ds.setSavedEmail("")
                            ds.markEmailPrompted(ui.email)
                            credsStore.clearCredentials(ui.email)
                        }
                        showRememberDialog = false
                        navController.navigate(ScreenNavigation.Home.route) {
                            popUpTo(ScreenNavigation.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }) {
                        Text(text = stringResource(id = R.string.remember_no))
                    }
                },
                title = { Text(text = stringResource(id = R.string.remember_title)) },
                text = { Text(text = stringResource(id = R.string.remember_message)) }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            // Brand/logo minimal
            Image(
                painter = painterResource(id = R.drawable.applogo),
                contentDescription = "ULSA logo",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.height(16.dp))
            Text(
                text = "Sign in",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )

            Spacer(Modifier.height(32.dp))

            // Email
            OutlinedTextField(
                value = ui.email,
                onValueChange = vm::onEmailChange,
                label = { Text(stringResource(R.string.email_label), color = Color.Gray) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.None,
                    autoCorrect = false
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color(0xFFDDDDDD),
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Gray,
                    disabledBorderColor = Color(0xFFEEEEEE)
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(12.dp))

            // Password
            OutlinedTextField(
                value = ui.password,
                onValueChange = vm::onPasswordChange,
                label = { Text(stringResource(R.string.password_label), color = Color.Gray) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.None,
                    autoCorrect = false
                ),
                keyboardActions = KeyboardActions(onDone = { vm.login() }),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = "Toggle password visibility", tint = Color.Black)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color(0xFFDDDDDD),
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Gray,
                    disabledBorderColor = Color(0xFFEEEEEE)
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(24.dp))

            // Login button - solid black
            Button(
                onClick = { vm.login() },
                enabled = !ui.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ) {
                if (ui.isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp),
                        color = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Signing in…")
                } else {
                    Text(stringResource(R.string.login_button))
                }
            }

            Spacer(Modifier.height(12.dp))

            // Biometric secondary action (auto-detect Face vs Fingerprint)
            OutlinedButton(
                onClick = {
                    if (biometricAvailable && biometricPrompt != null) {
                        biometricPrompt.authenticate(promptInfo)
                    } else {
                        showToastSafe("Biometric not available")
                    }
                },
                enabled = !ui.isLoading && biometricAvailable,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Black
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Color.Black)
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ) {
                val icon = if (modality == BiometricModality.FACE) Icons.Default.Face else Icons.Default.Fingerprint
                val label = if (modality == BiometricModality.FACE) "Face ID" else "Fingerprint"
                Icon(icon, contentDescription = label, modifier = Modifier.size(20.dp), tint = Color.Black)
                Spacer(Modifier.width(8.dp))
                Text(label)
            }

            Spacer(Modifier.weight(1f))

            // Minimal footer
            Text(
                text = "Gym4ULSA",
                style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.findActivity()
        else -> null
    }
}

@Suppress("unused")
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
