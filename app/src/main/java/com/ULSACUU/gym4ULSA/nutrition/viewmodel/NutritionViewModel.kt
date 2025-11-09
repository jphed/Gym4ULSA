package com.ULSACUU.gym4ULSA.nutrition.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ULSACUU.gym4ULSA.nutrition.data.NutritionNetwork
import com.ULSACUU.gym4ULSA.nutrition.data.NutritionRepository
import com.ULSACUU.gym4ULSA.nutrition.model.RootNutrition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class NutritionUiState {
    object Loading : NutritionUiState()
    data class Success(val data: RootNutrition) : NutritionUiState()
    data class Error(val message: String) : NutritionUiState()
}

class NutritionViewModel : ViewModel() {
    private val repo = NutritionRepository(NutritionNetwork.api)

    private val _uiState: MutableStateFlow<NutritionUiState> = MutableStateFlow(NutritionUiState.Loading)
    val uiState: StateFlow<NutritionUiState> = _uiState

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
}
