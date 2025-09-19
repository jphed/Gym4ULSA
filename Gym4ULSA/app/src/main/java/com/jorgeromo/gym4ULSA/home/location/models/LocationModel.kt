package com.jorgeromo.gym4ULSA.ids.location.models

data class LocationModel(
    val id: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String
)