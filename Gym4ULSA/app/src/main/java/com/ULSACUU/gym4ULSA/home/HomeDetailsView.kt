package com.ULSACUU.gym4ULSA.home

import Exercise
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ULSACUU.gym4ULSA.R

@Composable
fun HomeDetailsView(exercise: Exercise, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = getTranslatedData(exercise.nombre),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.details_repetitions, exercise.repeticiones),
            style = MaterialTheme.typography.bodyLarge
        )

        //val translatedCategory = getTranslatedData(exercise.categoria)

        //Text(
         //   text = stringResource(R.string.details_category, translatedCategory),
         //   style = MaterialTheme.typography.bodyLarge
       // )
    }}

/**
 * Función de mapeo para traducir los datos del Gist
 * (Si esta función no está en un archivo /utils, debes copiarla aquí)
 */
@Composable
private fun getTranslatedData(dataFromGist: String): String {
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

        else -> null
    }

    return if (resourceId != null) {
        stringResource(resourceId)
    } else {
        dataFromGist
    }
}