package com.ULSACUU.gym4ULSA.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ULSACUU.gym4ULSA.R

@Composable
fun HomeView(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val routines = viewModel.routines
    val selectedRoutine = viewModel.selectedRoutine
    val exercises = viewModel.exercisesForSelectedRoutine

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 8.dp)) {

        // 🔹 Encabezado de rutinas
        Text(
            text = stringResource(id = R.string.home_routines_title),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        // 🔹 Barra horizontal de rutinas con imágenes
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(routines) { routine ->
                Card(
                    onClick = { viewModel.selectRoutine(routine) },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .width(180.dp)
                        .padding(bottom = 4.dp)
                        .then(
                            if (routine == selectedRoutine)
                                Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                            else Modifier
                        )
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        // Imagen
                        Image(
                            painter = rememberAsyncImagePainter(routine.imagen),
                            contentDescription = getTranslatedData(routine.nombre),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = getTranslatedData(routine.nombre), fontWeight = FontWeight.Bold)
                        Text(
                            text = getTranslatedData(routine.musculo),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = routine.duracion,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.selectRoutine(routine) }
                        ) {
                            Text(stringResource(id = R.string.home_see_exercises))
                        }
                    }
                }
            }
        }

        // 🔹 Encabezado de ejercicios
        Text(
            text = stringResource(id = R.string.home_exercises_title),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
        )

        // 🔹 Lista vertical de ejercicios
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(exercises) { exercise ->
                Button(
                    onClick = {
                        navController.navigate("HomeDetailsRoute/${exercise.id}")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(getTranslatedData(exercise.nombre))
                }
            }
        }
    }
}

/**
 * Función de mapeo para traducir los datos del Gist
 * Traduce un texto que viene del Gist a su ID de recurso de string correspondiente.
 * Si no encuentra una traducción, devuelve el texto original.
 */
@Composable
private fun getTranslatedData(dataFromGist: String): String {
    // Busca el ID del string basándose en el texto en español del Gist
    val resourceId = when (dataFromGist) {
        // Rutinas
        "Rutina Pecho" -> R.string.routine_chest_name
        "Rutina Piernas" -> R.string.routine_legs_name
        "Rutina Espalda" -> R.string.routine_back_name

        // Descripciones
        "Ejercicios para pecho" -> R.string.routine_chest_desc
        "Ejercicios para piernas" -> R.string.routine_legs_desc
        "Ejercicios para espalda" -> R.string.routine_back_desc

        // Músculos / Categorías
        "Pecho" -> R.string.muscle_chest
        "Piernas" -> R.string.muscle_legs
        "Espalda" -> R.string.muscle_back
        "Brazos" -> R.string.muscle_arms
        "Hombros" -> R.string.muscle_shoulders

        // Ejercicios
        "Press banca" -> R.string.exercise_bench_press
        "Sentadilla" -> R.string.exercise_squat
        "Dominadas" -> R.string.exercise_pullups
        "Desplantes" -> R.string.exercise_lunges
        "Curl biceps" -> R.string.exercise_bicep_curl
        "Elevaciones laterales" -> R.string.exercise_lat_raises

        // Si no se encuentra, no hagas nada
        else -> null
    }

    // Devuelve el texto traducido si se encontró, o el original si no
    return if (resourceId != null) {
        stringResource(resourceId)
    } else {
        dataFromGist
    }
}