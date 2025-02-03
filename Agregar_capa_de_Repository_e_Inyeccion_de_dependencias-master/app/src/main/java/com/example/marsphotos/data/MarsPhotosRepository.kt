package com.example.marsphotos.data

import com.example.marsphotos.model.MarsPhoto
import com.example.marsphotos.network.MarsApiService

// Interfaz para el repositorio de fotos de Marte.
interface MarsPhotosRepository {
    suspend fun getMarsPhotos(): List<MarsPhoto>
}

// Implementación del repositorio que usa la API de Retrofit.
class NetworkMarsPhotosRepository(
    private val marsApiService: MarsApiService
) : MarsPhotosRepository {

    // Función que obtiene las fotos de Marte desde la API.
    override suspend fun getMarsPhotos(): List<MarsPhoto> = marsApiService.getPhotos()
}
