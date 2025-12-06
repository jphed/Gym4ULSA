package com.ULSACUU.gym4ULSA.sign_up.view


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ULSACUU.gym4ULSA.sign_up.model.NivelExperiencia
import com.ULSACUU.gym4ULSA.sign_up.model.ObjetivoFitness
import com.ULSACUU.gym4ULSA.sign_up.viewmodel.RegistroEvent
import com.ULSACUU.gym4ULSA.sign_up.viewmodel.RegistroViewModel


@Composable
fun RegistroScreen(
    viewModel: RegistroViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onRegistroExitoso: () -> Unit // Callback para navegar al Home
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Crea tu perfil",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )
        Text("Completa esta encuesta inicial para personalizar Gym4ULSA", fontSize = 14.sp, color = Color.Gray)

        // --- Credenciales ---
        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onEvent(RegistroEvent.EnteredEmail(it)) },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onEvent(RegistroEvent.EnteredPassword(it)) },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        // --- Datos Biométricos (Fila 1) ---
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = state.edad,
                onValueChange = { if (it.all { char -> char.isDigit() }) viewModel.onEvent(RegistroEvent.EnteredEdad(it)) },
                label = { Text("Edad") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = state.altura,
                onValueChange = { if (it.all { char -> char.isDigit() }) viewModel.onEvent(RegistroEvent.EnteredAltura(it)) },
                label = { Text("Altura (cm)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        // --- Datos Biométricos (Fila 2) ---
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = state.pesoActual,
                onValueChange = { viewModel.onEvent(RegistroEvent.EnteredPeso(it)) },
                label = { Text("Peso (kg)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            OutlinedTextField(
                value = state.metaPeso,
                onValueChange = { viewModel.onEvent(RegistroEvent.EnteredMeta(it)) },
                label = { Text("Meta (kg)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        }

        Divider()

        // --- Selectores (ENUMS) ---

        Text("Nivel de Experiencia", modifier = Modifier.align(Alignment.Start))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            NivelExperiencia.values().forEach { nivel ->
                FilterChip(
                    selected = state.nivelExperiencia == nivel,
                    onClick = { viewModel.onEvent(RegistroEvent.SelectedExperiencia(nivel)) },
                    label = { Text(nivel.label) }
                )
            }
        }

        Text("Objetivo Fitness", modifier = Modifier.align(Alignment.Start))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            ObjetivoFitness.values().forEach { obj ->
                FilterChip(
                    selected = state.objetivoFitness == obj,
                    onClick = { viewModel.onEvent(RegistroEvent.SelectedObjetivo(obj)) },
                    label = { Text(obj.label) }
                )
            }
        }

        // --- Legal ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = state.terminosAceptados,
                onCheckedChange = { viewModel.onEvent(RegistroEvent.ToggleTerminos(it)) }
            )
            Text("Acepto términos y privacidad")
        }

        if (state.error != null) {
            Text(state.error!!, color = Color.Red, fontSize = 12.sp)
        }

        Button(
            onClick = { viewModel.onEvent(RegistroEvent.Submit) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) CircularProgressIndicator(color = Color.White)
            else Text("Finalizar Registro")
        }
    }
}