package com.ULSACUU.gym4ULSA.home

import kotlinx.serialization.Serializable
import com.google.gson.annotations.SerializedName

@Serializable
data class RoutinesResponse(
    @SerializedName("rutinas")
    val rutinas: List<Routine>,
    @SerializedName("ejercicios")
    val ejercicios: List<Exercise>
)

@Serializable
data class Routine(
    val id: Int,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("descripcion")
    val descripcion: String,
    val musculo: String,
    val imagen: String,
    val duracion: String
)

@Serializable
data class Exercise(
    val id: Int,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("repeticiones")
    val repeticiones: String,
    @SerializedName("categoria")
    val categoria: String? = null
)
