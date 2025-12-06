package com.ULSACUU.gym4ULSA.sign_up.model


enum class NivelExperiencia(val label: String) {
    PRINCIPIANTE("Principiante"),
    INTERMEDIO("Intermedio"),
    AVANZADO("Avanzado")
}

enum class ObjetivoFitness(val label: String) {
    DEFICIT("Déficit calórico"),
    VOLUMEN("Volumen")
}

enum class Genero(val label: String) {
    MASCULINO("Masculino"),
    FEMENINO("Femenino"),
    OTRO("Otro/Prefiero no decir")
}

// Estado del formulario para el ViewModel
data class RegistroState(
    val email: String = "",
    val password: String = "",
    val edad: String = "",
    val pesoActual: String = "",
    val altura: String = "",
    val metaPeso: String = "",
    val nivelExperiencia: NivelExperiencia = NivelExperiencia.PRINCIPIANTE,
    val objetivoFitness: ObjetivoFitness = ObjetivoFitness.DEFICIT,
    val genero: Genero? = null,
    val terminosAceptados: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)