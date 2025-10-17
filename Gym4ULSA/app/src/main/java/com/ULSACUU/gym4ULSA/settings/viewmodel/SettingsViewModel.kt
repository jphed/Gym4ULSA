package com.ULSACUU.gym4ULSA.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ULSACUU.gym4ULSA.utils.UserPreferencesDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val prefs: UserPreferencesDataStore
) : ViewModel() {

    val isDarkTheme = prefs.isDarkTheme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    val language = prefs.language.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = "es"
    )

    fun toggleTheme() {
        viewModelScope.launch {
            prefs.setDarkTheme(!isDarkTheme.value)
        }
    }

    fun changeLanguage(lang: String) {
        viewModelScope.launch {
            prefs.setLanguage(lang)
        }
    }
}
