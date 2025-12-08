package com.ULSACUU.gym4ULSA.nutrition.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ULSACUU.gym4ULSA.R
import com.ULSACUU.gym4ULSA.nutrition.model.Food
import com.ULSACUU.gym4ULSA.nutrition.viewmodel.AnalysisState
import com.ULSACUU.gym4ULSA.nutrition.viewmodel.NutritionUiState
import com.ULSACUU.gym4ULSA.nutrition.viewmodel.NutritionViewModel
import com.ULSACUU.gym4ULSA.nutrition.viewmodel.TrackedFoodEntry
import kotlin.math.max
import kotlin.math.min
import java.util.Locale
import androidx.core.content.ContextCompat

@Composable
fun NutritionView(navController: NavController, vm: NutritionViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()
    val analysisState by vm.analysisState.collectAsState()
    val trackedFoods by vm.trackedFoods.collectAsState()

    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            vm.analyzeFoodImage(bitmap)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val permissionStatus = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    )

                    if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(null)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                containerColor = Color(0xFF0A84FF),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Escanear comida")
            }
        }
    ) { paddingValues ->
        // Contenedor principal con padding del Scaffold
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val s = state) {
                is NutritionUiState.Loading -> LoadingSection()
                is NutritionUiState.Error -> ErrorSection(message = s.message, onRetry = vm::refresh)
                is NutritionUiState.Success -> NutritionContent(
                    state = s,
                    trackedFoods = trackedFoods,
                    onAddFood = vm::addFoodToDashboard,
                    onRemoveFood = vm::removeTrackedFood
                )
            }
        }
    }

    // --- Manejo de Diálogos y Estados de IA ---

    when (val anaState = analysisState) {
        is AnalysisState.Loading -> {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Consultando a la IA...") },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(16.dp))
                        Text("Analizando imagen...")
                    }
                },
                confirmButton = {}
            )
        }
        is AnalysisState.Error -> {
            AlertDialog(
                onDismissRequest = { vm.resetAnalysisState() },
                title = { Text("Ups!") },
                text = { Text(anaState.message) },
                confirmButton = {
                    TextButton(onClick = { vm.resetAnalysisState() }) { Text("Cerrar") }
                }
            )
        }
        is AnalysisState.Success -> {
            // Este es el "Resumen" del diagrama
            FoodConfirmationDialog(
                result = anaState.result,
                onConfirm = {
                    vm.saveFoodToDatabase(anaState.result)
                },
                onDismiss = { vm.resetAnalysisState() }
            )
        }
        else -> {}
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
        Text(stringResource(R.string.nutrition_loading), style = MaterialTheme.typography.titleMedium)
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
        Text(stringResource(R.string.nutrition_error_title), style = MaterialTheme.typography.titleMedium)
        Text(message, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Button(onClick = onRetry) { Text(stringResource(R.string.nutrition_retry)) }
    }
}

@Composable
private fun NutritionContent(
    state: NutritionUiState.Success,
    trackedFoods: List<TrackedFoodEntry>,
    onAddFood: (Food) -> Unit,
    onRemoveFood: (String) -> Unit
) {
    val data = state.data
    val settings = data.nutrition_settings
    val macroTargets = settings?.macro_targets
    val targetKcal = settings?.calorie_target_kcal ?: 2050
    val totals = remember(trackedFoods) { trackedFoods.toMacroTotals() }
    val consumedKcal = totals.calories
    val progress = if (targetKcal > 0) min(1f, max(0f, consumedKcal.toFloat() / targetKcal)) else 0f

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Header con gradiente y anillo de calorías
        item {
            HeaderSection(
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
                proteinConsumed = totals.protein.toFloat(),
                carbsConsumed = totals.carbs.toFloat(),
                fatConsumed = totals.fat.toFloat()
            )
        }

        item {
            Spacer(Modifier.height(12.dp))
            FoodSelectionSection(
                foods = data.food_database.orEmpty(),
                onAddFood = onAddFood
            )
        }

        item {
            Spacer(Modifier.height(12.dp))
            DailySummarySection(
                totals = totals,
                trackedFoods = trackedFoods,
                onRemoveFood = onRemoveFood
            )
        }

        // Lista de alimentos
        val foods = data.food_database.orEmpty()
        if (foods.isNotEmpty()) {
            item { SectionTitle(text = stringResource(R.string.nutrition_foods_title)) }
            items(foods.take(10)) { food ->
                FoodCard(food = food)
            }
        }

        // Suplementos
        val supps = data.supplements_catalog.orEmpty()
        if (supps.isNotEmpty()) {
            item { SectionTitle(text = stringResource(R.string.nutrition_supplements_title)) }
            items(supps) { sp ->
                val locale = Locale.getDefault().language
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
                            contentDescription = sp.name.get(locale),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x11FFFFFF))
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(sp.name.get(locale), style = MaterialTheme.typography.titleMedium)
                            val brand = sp.brand?.get(locale) ?: stringResource(id = R.string.generic_brand)
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
    targetKcal: Int,
    consumedKcal: Int,
    progress: Float
) {
    val title = stringResource(R.string.nutrition_title)
    val subtitle = stringResource(R.string.nutrition_subtitle)
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
                        text = "${stringResource(R.string.nutrition_of)} $targetKcal ${stringResource(R.string.nutrition_kcal)}",
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
            Text(stringResource(R.string.nutrition_macros_title), style = MaterialTheme.typography.titleMedium)
            MacroBar(stringResource(R.string.nutrition_protein), proteinConsumed, proteinTarget, Color(0xFF34C759))
            MacroBar(stringResource(R.string.nutrition_carbs), carbsConsumed, carbsTarget, Color(0xFF0A84FF))
            MacroBar(stringResource(R.string.nutrition_fat), fatConsumed, fatTarget, Color(0xFFFF9F0A))
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
    val locale = Locale.getDefault().language
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
                contentDescription = food.name.get(locale),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x11FFFFFF))
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(food.name.get(locale), style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                val kcal100 = food.per_100g.energy_kcal?.toInt() ?: 0
                Text(stringResource(R.string.nutrition_per_100g, kcal100), color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    food.tags?.get(locale)?.firstOrNull()?.let { TagChip(text = it) }
                    food.servings.firstOrNull()?.let { TagChip(text = it.label.get(locale)) }
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

@Composable
private fun FoodSelectionSection(
    foods: List<Food>,
    onAddFood: (Food) -> Unit
) {
    val locale = Locale.getDefault().language
    var expanded by remember { mutableStateOf(false) }
    var selectedFood by remember { mutableStateOf<Food?>(null) }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x0DFFFFFF))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = stringResource(R.string.nutrition_add_food_title),
                style = MaterialTheme.typography.titleMedium
            )
            Box {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x12FFFFFF))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .clickable(enabled = foods.isNotEmpty()) { expanded = true }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedFood?.name?.get(locale)
                                ?: stringResource(R.string.nutrition_add_food_placeholder),
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (selectedFood == null) Color.Gray else Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    foods.take(50).forEach { food ->
                        DropdownMenuItem(
                            text = { Text(food.name.get(locale)) },
                            onClick = {
                                selectedFood = food
                                expanded = false
                            }
                        )
                    }
                }
            }
            Button(
                onClick = { selectedFood?.let(onAddFood) },
                enabled = selectedFood != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.nutrition_add_food_button))
            }
        }
    }
}

@Composable
private fun DailySummarySection(
    totals: MacroTotals,
    trackedFoods: List<TrackedFoodEntry>,
    onRemoveFood: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x14FFFFFF))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(stringResource(R.string.nutrition_daily_summary_title), style = MaterialTheme.typography.titleMedium)

            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                MacroItemValue("Kcal", "${totals.calories}")
                MacroItemValue("Prot", "${totals.protein} g", Color(0xFF34C759))
                MacroItemValue("Carb", "${totals.carbs} g", Color(0xFF0A84FF))
                MacroItemValue("Grasa", "${totals.fat} g", Color(0xFFFF9F0A))
            }

            Divider(color = Color.White.copy(alpha = 0.1f))

            if (trackedFoods.isEmpty()) {
                Text(
                    text = stringResource(R.string.nutrition_daily_summary_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    trackedFoods.forEach { entry ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x10FFFFFF))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(entry.foodName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "${entry.calories} kcal • ${entry.protein}P / ${entry.carbs}C / ${entry.fat}F",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            IconButton(onClick = { onRemoveFood(entry.id) }) {
                                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.nutrition_remove_food))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FoodConfirmationDialog(
    result: com.ULSACUU.gym4ULSA.nutrition.viewmodel.AIAnalysisResult, // Ajusta el import según tu package
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Resultado del Análisis") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = result.foodName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(result.summary, style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                Divider(Modifier.padding(vertical = 8.dp))

                // Resumen de Macros rápido
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    MacroItemValue("Kcal", "${result.calories}")
                    MacroItemValue("Prot", "${result.protein}g", Color(0xFF34C759))
                    MacroItemValue("Carb", "${result.carbs}g", Color(0xFF0A84FF))
                    MacroItemValue("Grasa", "${result.fat}g", Color(0xFFFF9F0A))
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Aceptar y Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun MacroItemValue(label: String, value: String, color: Color = Color.White) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = color)
    }
}

private data class MacroTotals(
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int
)

private fun List<TrackedFoodEntry>.toMacroTotals(): MacroTotals {
    return fold(MacroTotals(0, 0, 0, 0)) { acc, entry ->
        MacroTotals(
            calories = acc.calories + entry.calories,
            protein = acc.protein + entry.protein,
            carbs = acc.carbs + entry.carbs,
            fat = acc.fat + entry.fat
        )
    }
}
