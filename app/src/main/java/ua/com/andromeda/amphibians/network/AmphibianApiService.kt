package ua.com.andromeda.amphibians.network

import retrofit2.http.GET

interface AmphibianApiService {
    @GET("amphibians")
    suspend fun findAll(): List<Amphibian>
}