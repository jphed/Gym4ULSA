package com.ULSACUU.gym4ULSA.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun HomeView(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val routines = viewModel.routines
    val selectedRoutine = viewModel.selectedRoutine
    val exercises = viewModel.exercisesForSelectedRoutine

    Column {
        // Barra horizontal de rutinas
        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            items(routines) { routine ->
                Button(
                    onClick = { viewModel.selectRoutine(routine) },
                    colors = if (routine == selectedRoutine)
                        ButtonDefaults.buttonColors(containerColor = Color.Blue)
                    else ButtonDefaults.buttonColors(),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(routine.nombre)
                }
            }
        }

        Row(modifier = Modifier.fillMaxSize()) {
            // Barra vertical de ejercicios
            LazyColumn(
                modifier = Modifier
                    .width(150.dp)
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(exercises) { exercise ->
                    Button(
                        onClick = {
                            // Navega a la pantalla de detalle
                            navController.navigate("HomeDetailsRoute/${exercise.id}")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(exercise.nombre)
                    }
                }
            }
        }
    }
}
