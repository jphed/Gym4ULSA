package com.ULSACUU.gym4ULSA.create_routine.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ULSACUU.gym4ULSA.create_routine.viewmodel.CreateRoutineViewModel
import com.ULSACUU.gym4ULSA.home.Exercise
import com.ULSACUU.gym4ULSA.R
import com.ULSACUU.gym4ULSA.home.getTranslatedData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoutineScreen(
    navController: NavController,
    viewModel: CreateRoutineViewModel = viewModel()
) {
    val bottomSheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.create_routine_title)) }, // INTERNACIONALIZADO
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.content_description_back) // INTERNACIONALIZADO
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            viewModel.saveRoutine {
                                navController.popBackStack()
                            }
                        },
                        // Se activa solo si el nombre no está vacío y hay ejercicios
                        enabled = viewModel.routineName.isNotBlank() && viewModel.addedExercises.isNotEmpty()
                    ) {
                        Text(stringResource(R.string.action_save)) // INTERNACIONALIZADO
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // --- Nombre de la Rutina ---
            OutlinedTextField(
                value = viewModel.routineName,
                onValueChange = { viewModel.routineName = it },
                label = { Text(stringResource(R.string.create_routine_name_label)) }, // INTERNACIONALIZADO
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            // --- Lista de Ejercicios Añadidos ---
            Text(
                text = stringResource(R.string.create_routine_exercises_added),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(viewModel.addedExercises) { exercise ->
                    AddedExerciseItem(
                        exercise = exercise,
                        onRemove = { viewModel.removeExercise(exercise) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // --- Botón para abrir el Sheet ---
            Button(
                onClick = { viewModel.isSheetOpen = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text(stringResource(R.string.create_routine_add_button))
            }
        }
    }

    // --- Bottom Sheet para Seleccionar Ejercicios ---
    if (viewModel.isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.isSheetOpen = false },
            sheetState = bottomSheetState
        ) {
            LazyColumn(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = 24.dp)
            ) {
                item {
                    Text(
                        stringResource(R.string.create_routine_select_exercise),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(viewModel.allExercises) { exercise ->
                    ExerciseSelectionItem(
                        exercise = exercise,
                        onAdd = { viewModel.addExercise(exercise) }
                    )
                }
            }
        }
    }
}

// --- Componente de la lista modificado ---

@Composable
fun AddedExerciseItem(exercise: Exercise, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = getTranslatedData(exercise.nombre),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = stringResource(R.string.content_description_remove)
                )
            }
        }
    }
}

@Composable
fun ExerciseSelectionItem(exercise: Exercise, onAdd: () -> Unit) {
    ListItem(
        headlineContent = { Text(getTranslatedData(exercise.nombre)) },
        supportingContent = { Text(getTranslatedData(exercise.categoria ?: "")) },
        modifier = Modifier.clickable { onAdd() }
    )
}