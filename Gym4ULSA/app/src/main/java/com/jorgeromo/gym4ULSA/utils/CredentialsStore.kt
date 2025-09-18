package com.jorgeromo.gym4ULSA.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class CredentialsStore(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "credentials_store",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun setCredentials(email: String, password: String) {
        prefs.edit()
            .putString("email", email)
            .putString(passwordKey(email), password)
            .apply()
    }

    fun getSavedEmail(): String? = prefs.getString("email", null)

    fun getPassword(email: String): String? = prefs.getString(passwordKey(email), null)

    fun clearCredentials(email: String) {
        prefs.edit()
            .remove("email")
            .remove(passwordKey(email))
            .apply()
    }

    private fun passwordKey(email: String) = "pwd_${email.lowercase()}"
}
