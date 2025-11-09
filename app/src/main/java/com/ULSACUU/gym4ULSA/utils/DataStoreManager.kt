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
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val ACCOUNT_CREATED_AT = stringPreferencesKey("account_created_at")
        val USER_PHOTO_URI = stringPreferencesKey("user_photo_uri")
    }

    // Onboarding
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

    // Saved email
    val savedEmailFlow: Flow<String> =
        context.dataStore.data.map { prefs -> prefs[Keys.SAVED_EMAIL] ?: "" }

    suspend fun setSavedEmail(email: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SAVED_EMAIL] = email
        }
    }

    // Prompted emails
    val promptedEmailsFlow: Flow<Set<String>> =
        context.dataStore.data.map { prefs -> prefs[Keys.PROMPTED_EMAILS] ?: emptySet() }

    suspend fun markEmailPrompted(email: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.PROMPTED_EMAILS] ?: emptySet()
            prefs[Keys.PROMPTED_EMAILS] = current + email
        }
    }

    // User profile fields
    val userNameFlow: Flow<String> =
        context.dataStore.data.map { prefs -> prefs[Keys.USER_NAME] ?: "" }

    val userEmailFlow: Flow<String> =
        context.dataStore.data.map { prefs -> prefs[Keys.USER_EMAIL] ?: "" }

    val accountCreatedAtFlow: Flow<String> =
        context.dataStore.data.map { prefs -> prefs[Keys.ACCOUNT_CREATED_AT] ?: "" }

    val userPhotoUriFlow: Flow<String> =
        context.dataStore.data.map { prefs -> prefs[Keys.USER_PHOTO_URI] ?: "" }

    suspend fun setUserName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.USER_NAME] = name
        }
    }

    suspend fun setUserEmail(email: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.USER_EMAIL] = email
        }
    }

    suspend fun setAccountCreatedAt(date: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.ACCOUNT_CREATED_AT] = date
        }
    }

    suspend fun setUserPhotoUri(uri: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.USER_PHOTO_URI] = uri
        }
    }
}

class UserPreferencesDataStore(private val context: Context) {

    companion object {
        val THEME_DARK = booleanPreferencesKey("theme_dark")
        val LANGUAGE = stringPreferencesKey("language")
    }

    val isDarkTheme: Flow<Boolean> = context.dataStore.data.map {
        it[THEME_DARK] ?: false
    }

    val language: Flow<String> = context.dataStore.data.map {
        it[LANGUAGE] ?: "es"
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { it[THEME_DARK] = enabled }
    }

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { it[LANGUAGE] = lang }
    }
}

