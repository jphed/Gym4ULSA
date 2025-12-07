package com.ULSACUU.gym4ULSA.nutrition.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ULSACUU.gym4ULSA.nutrition.data.NutritionNetwork
import com.ULSACUU.gym4ULSA.nutrition.data.NutritionRepository
import com.ULSACUU.gym4ULSA.nutrition.model.RootNutrition
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

// Flujo de IA ---

data class AIAnalysisResult(
    val foodName: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val summary: String
)

sealed class AnalysisState {
    object Idle : AnalysisState()
    object Loading : AnalysisState()
    data class Success(val result: AIAnalysisResult) : AnalysisState()
    data class Error(val message: String) : AnalysisState()
}


sealed class NutritionUiState {
    object Loading : NutritionUiState()
    data class Success(val data: RootNutrition) : NutritionUiState()
    data class Error(val message: String) : NutritionUiState()
}

class NutritionViewModel : ViewModel() {
    private val repo = NutritionRepository(NutritionNetwork.api)

    // Datos de la API normal
    private val _uiState: MutableStateFlow<NutritionUiState> = MutableStateFlow(NutritionUiState.Loading)
    val uiState: StateFlow<NutritionUiState> = _uiState

    // Proceso de IA
    private val _analysisState = MutableStateFlow<AnalysisState>(AnalysisState.Idle)
    val analysisState: StateFlow<AnalysisState> = _analysisState.asStateFlow()

    // Configuración de Gemini
    val generativeModel = GenerativeModel(
        modelName = "models/gemini-2.5-flash",
        apiKey = "AIzaSyAW2Zu9641N3MlyxNexFIz-EoNFYP0kbNg"
    )

    init {
        refresh()
    }

    fun refresh() {
        _uiState.value = NutritionUiState.Loading
        viewModelScope.launch {
            try {
                val data = repo.fetchNutrition()
                _uiState.value = NutritionUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = NutritionUiState.Error(e.message ?: "Error al cargar nutrición")
            }
        }
    }

    // Funciones para la IA

    fun resetAnalysisState() {
        _analysisState.value = AnalysisState.Idle
    }

    fun analyzeFoodImage(bitmap: Bitmap) {
        _analysisState.value = AnalysisState.Loading

        viewModelScope.launch {
            try {
                // Prompt de Ingeniería: Para asegurar JSON
                val prompt = """
                    Analiza esta imagen de comida. Identifica el plato principal y estima sus macros.
                    Responde EXCLUSIVAMENTE con un JSON válido. 
                    NO incluyas bloques de código markdown (```json). SOLO el objeto JSON.
                    Formato requerido:
                    {
                        "food_name": "Nombre corto del plato",
                        "calories": 0,
                        "protein_g": 0,
                        "carbs_g": 0,
                        "fat_g": 0,
                        "summary": "Breve descripción de 1 linea"
                    }
                """.trimIndent()

                val response = generativeModel.generateContent(
                    content {
                        image(bitmap)
                        text(prompt)
                    }
                )

                // Limpieza de respuesta
                val responseText = response.text?.replace("```json", "")?.replace("```", "")?.trim()

                if (responseText != null) {
                    val json = JSONObject(responseText)

                    val result = AIAnalysisResult(
                        foodName = json.getString("food_name"),
                        calories = json.getInt("calories"),
                        protein = json.getInt("protein_g"),
                        carbs = json.getInt("carbs_g"),
                        fat = json.getInt("fat_g"),
                        summary = json.optString("summary", "Alimento identificado")
                    )

                    _analysisState.value = AnalysisState.Success(result)
                } else {
                    _analysisState.value = AnalysisState.Error("No pude identificar el alimento.")
                }

            } catch (e: Exception) {
                _analysisState.value = AnalysisState.Error("Error de conexión con IA: ${e.localizedMessage}")
            }
        }
    }

    fun saveFoodToDatabase(result: AIAnalysisResult) {
        viewModelScope.launch {
            try {
                // TODO: Aquí conectarías con tu Repo para guardar en Backend
                // repo.postFoodLog(result)

                // Por ahora, solo simulamos y refrescamos
                resetAnalysisState()

                // Opcional: Llamar a refresh() si el backend actualizó los totales
                // refresh()

            } catch (e: Exception) {
                // Manejar error de guardado
            }
        }
    }
}
