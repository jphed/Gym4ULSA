package com.ULSACUU.gym4ULSA.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.ULSACUU.gym4ULSA.R

@Composable
fun HomeDetailsView(exercise: Exercise, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(text = exercise.nombre, style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.home_repetitions, exercise.repeticiones))
        Text(text = stringResource(id = R.string.home_category, exercise.categoria ?: ""))
    }
}

