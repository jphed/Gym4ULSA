package com.ULSACUU.gym4ULSA.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DS_NAME = "app_prefs_ds"
val Context.dataStore by preferencesDataStore(DS_NAME)

class DataStoreManager(private val context: Context) {

    private object Keys {
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val REMEMBER_CREDENTIALS = booleanPreferencesKey("remember_credentials")
        val SAVED_EMAIL = stringPreferencesKey("saved_email")
        val PROMPTED_EMAILS = stringSetPreferencesKey("prompted_emails")
    }

    val onboardingDoneFlow: Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[Keys.ONBOARDING_DONE] ?: false }

    suspend fun setOnboardingDone(done: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.ONBOARDING_DONE] = done
        }
    }

    // Remember credentials flag
    val rememberCredentialsFlow: Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[Keys.REMEMBER_CREDENTIALS] ?: false }

    suspend fun setRememberCredentials(remember: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.REMEMBER_CREDENTIALS] = remember
        }
    }

    // Saved email (we avoid storing password for security)
    val savedEmailFlow: Flow<String> =
        context.dataStore.data.map { prefs -> prefs[Keys.SAVED_EMAIL] ?: "" }

    suspend fun setSavedEmail(email: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SAVED_EMAIL] = email
        }
    }

    // Track which emails have already seen the remember dialog
    val promptedEmailsFlow: Flow<Set<String>> =
        context.dataStore.data.map { prefs -> prefs[Keys.PROMPTED_EMAILS] ?: emptySet() }

    suspend fun markEmailPrompted(email: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.PROMPTED_EMAILS] ?: emptySet()
            prefs[Keys.PROMPTED_EMAILS] = current + email
        }
    }
}