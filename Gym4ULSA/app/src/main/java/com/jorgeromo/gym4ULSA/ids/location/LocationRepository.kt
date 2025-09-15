package com.jorgeromo.gym4ULSA.ids.location

import com.jorgeromo.gym4ULSA.ids.location.models.LocationModel

class LocationRepository(private val apiService: LocationApiService) {
    suspend fun fetchLocations(): List<LocationModel> {
        return apiService.getLocations()
    }
}