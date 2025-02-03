package com.example.marsphotos.data

import retrofit2.Retrofit
import com.example.marsphotos.network.MarsApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

// Interfaz para el contenedor de la aplicación.
interface AppContainer {
    val marsPhotosRepository: MarsPhotosRepository
}

// Implementación predeterminada del contenedor de la aplicación.
class DefaultAppContainer : AppContainer {

    private val baseUrl = "https://android-kotlin-fun-mars-server.appspot.com"

    // Se construye el objeto Retrofit con el conversor de Kotlinx Serialization.
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    // Se crea el servicio Retrofit para acceder a la API.
    private val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }

    // Se define el repositorio de fotos de Marte.
    override val marsPhotosRepository: MarsPhotosRepository by lazy {
        NetworkMarsPhotosRepository(retrofitService)
    }
}
