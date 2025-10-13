package com.ULSACUU.gym4ULSA.home

import com.google.gson.annotations.SerializedName

data class RoutinesResponse(
    @SerializedName("rutinas")
    val rutinas: List<Routine>,
    @SerializedName("ejercicios")
    val ejercicios: List<Exercise>
)

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

data class Exercise(
    val id: Int,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("repeticiones")
    val repeticiones: String,
    @SerializedName("categoria")
    val categoria: String? = null
)
