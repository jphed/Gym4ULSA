package com.ULSACUU.gym4ULSA.nutrition.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ULSACUU.gym4ULSA.nutrition.viewmodel.NutritionViewModel

@Composable
fun NutritionView(navController: NavController, vm: NutritionViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Nutrition",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = vm.subtitle,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
