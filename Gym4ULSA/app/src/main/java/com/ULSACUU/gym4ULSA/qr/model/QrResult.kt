package com.ULSACUU.gym4ULSA.qr.model

/**
 * Data model for QR scan results
 */
data class QrResult(
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val format: String? = null
)