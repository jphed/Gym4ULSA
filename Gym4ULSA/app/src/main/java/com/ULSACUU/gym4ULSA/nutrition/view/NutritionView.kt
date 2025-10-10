package com.ULSACUU.gym4ULSA.nutrition.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.ULSACUU.gym4ULSA.nutrition.model.Food
import com.ULSACUU.gym4ULSA.nutrition.viewmodel.NutritionUiState
import com.ULSACUU.gym4ULSA.nutrition.viewmodel.NutritionViewModel
import kotlin.math.max
import kotlin.math.min

@Composable
fun NutritionView(navController: NavController, vm: NutritionViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()

    when (val s = state) {
        is NutritionUiState.Loading -> LoadingSection()
        is NutritionUiState.Error -> ErrorSection(message = s.message, onRetry = vm::refresh)
        is NutritionUiState.Success -> NutritionContent(s)
    }
}

@Composable
private fun LoadingSection() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Cargando datos…", style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun ErrorSection(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ocurrió un error", style = MaterialTheme.typography.titleMedium)
        Text(message, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Button(onClick = onRetry) { Text("Reintentar") }
    }
}

@Composable
private fun NutritionContent(state: NutritionUiState.Success) {
    val data = state.data
    val settings = data.nutrition_settings
    val macroTargets = settings?.macro_targets
    val targetKcal = settings?.calorie_target_kcal ?: 2050
    val consumedKcal = data.meal_plan_day?.summary?.energy_kcal?.toInt() ?: 1200
    val progress = if (targetKcal > 0) min(1f, max(0f, consumedKcal.toFloat() / targetKcal)) else 0f

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Header con gradiente y anillo de calorías
        item {
            HeaderSection(
                title = "Tu Balance Diario",
                subtitle = "Energía consumida",
                targetKcal = targetKcal,
                consumedKcal = consumedKcal,
                progress = progress
            )
        }

        // Tarjeta de macros con barras
        item {
            Spacer(Modifier.height(12.dp))
            MacroCard(
                proteinTarget = macroTargets?.protein_g?.toFloat() ?: 154f,
                carbsTarget = macroTargets?.carbs_g?.toFloat() ?: 205f,
                fatTarget = macroTargets?.fat_g?.toFloat() ?: 68f,
                proteinConsumed = (data.meal_plan_day?.summary?.protein_g ?: 85.0).toFloat(),
                carbsConsumed = (data.meal_plan_day?.summary?.carbs_g ?: 120.0).toFloat(),
                fatConsumed = (data.meal_plan_day?.summary?.fat_g ?: 45.0).toFloat()
            )
        }

        // Lista de alimentos
        val foods = data.food_database.orEmpty()
        if (foods.isNotEmpty()) {
            item { SectionTitle(text = "Alimentos sugeridos") }
            items(foods.take(10)) { food ->
                FoodCard(food = food)
            }
        }

        // Suplementos
        val supps = data.supplements_catalog.orEmpty()
        if (supps.isNotEmpty()) {
            item { SectionTitle(text = "Suplementos") }
            items(supps) { sp ->
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0x14FFFFFF)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(sp.image_url)
                                .crossfade(true)
                                .build(),
                            contentDescription = sp.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x11FFFFFF))
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(sp.name, style = MaterialTheme.typography.titleMedium)
                            val brand = sp.brand ?: "Genérico"
                            val price = sp.price?.value?.let { "$${"%.0f".format(it)} ${sp.price.currency ?: "MXN"}" } ?: ""
                            Text("$brand  •  $price", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    title: String,
    subtitle: String,
    targetKcal: Int,
    consumedKcal: Int,
    progress: Float
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A84FF),
                        Color(0xFF0066CC)
                    ),
                    startY = 0f,
                    endY = 800f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                title,
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = "$consumedKcal",
                        style = MaterialTheme.typography.displaySmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "de $targetKcal kcal",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    )
                }
                CalorieRing(size = 120.dp, stroke = 14.dp, progress = progress)
            }
        }
    }
}

@Composable
private fun CalorieRing(size: Dp, stroke: Dp, progress: Float) {
    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Background ring
        Canvas(modifier = Modifier.size(size)) {
            drawCircle(
                color = Color.White.copy(alpha = 0.2f),
                radius = (size.toPx() - stroke.toPx()) / 2f,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke.toPx())
            )
        }
        // Progress ring with gradient
        Canvas(modifier = Modifier.size(size)) {
            val sweepAngle = 360f * progress
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFF0F0F0),
                        Color(0xFFFFFFFF)
                    )
                ),
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = stroke.toPx(),
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )
            )
        }
        // Percentage text
        Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun MacroCard(
    proteinTarget: Float,
    carbsTarget: Float,
    fatTarget: Float,
    proteinConsumed: Float,
    carbsConsumed: Float,
    fatConsumed: Float
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0x0DFFFFFF)),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Macros del día", style = MaterialTheme.typography.titleMedium)
            MacroBar("Proteína", proteinConsumed, proteinTarget, Color(0xFF34C759))
            MacroBar("Carbohidratos", carbsConsumed, carbsTarget, Color(0xFF0A84FF))
            MacroBar("Grasas", fatConsumed, fatTarget, Color(0xFFFF9F0A))
        }
    }
}

@Composable
private fun MacroBar(label: String, value: Float, target: Float, color: Color) {
    val pct = if (target > 0f) min(1f, max(0f, value / target)) else 0f
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text("${value.toInt()} / ${target.toInt()} g", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(Color(0x1FFFFFFF))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(pct)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(999.dp))
                    .background(color)
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text,
        modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 4.dp),
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
    )
}

@Composable
private fun FoodCard(food: Food) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0x0DFFFFFF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(food.image_url)
                    .crossfade(true)
                    .build(),
                contentDescription = food.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x11FFFFFF))
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(food.name, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                val kcal100 = food.per_100g.energy_kcal?.toInt() ?: 0
                Text("${kcal100} kcal / 100g", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TagChip(text = food.tags?.firstOrNull() ?: "Alimento")
                    food.servings.firstOrNull()?.let { TagChip(text = it.label) }
                }
            }
        }
    }
}

@Composable
private fun TagChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color(0x18FFFFFF))
            .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(text, style = MaterialTheme.typography.bodySmall, color = Color.White)
    }
}
