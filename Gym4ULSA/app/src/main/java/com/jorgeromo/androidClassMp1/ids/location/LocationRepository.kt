package com.jorgeromo.androidClassMp1.ids.location

import com.jorgeromo.androidClassMp1.ids.location.models.LocationModel

class LocationRepository(private val apiService: LocationApiService) {
    suspend fun fetchLocations(): List<LocationModel> {
        return apiService.getLocations()
    }
}