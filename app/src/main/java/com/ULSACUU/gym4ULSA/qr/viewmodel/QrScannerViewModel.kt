package com.ULSACUU.gym4ULSA.qr.viewmodel

import androidx.lifecycle.ViewModel
import com.ULSACUU.gym4ULSA.qr.model.QrResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel for QR Scanner
 */
class QrScannerViewModel : ViewModel() {
    
    private val _scanResult = MutableStateFlow<QrResult?>(null)
    val scanResult: StateFlow<QrResult?> = _scanResult.asStateFlow()
    
    private val _isScanning = MutableStateFlow(true)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()
    
    fun onQrScanned(content: String, format: String? = null) {
        _scanResult.value = QrResult(
            content = content,
            format = format
        )
        _isScanning.value = false
    }
    
    fun resetScanner() {
        _scanResult.value = null
        _isScanning.value = true
    }
    
    fun stopScanning() {
        _isScanning.value = false
    }
}
