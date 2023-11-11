package ua.com.andromeda.amphibians.data

import ua.com.andromeda.amphibians.network.Amphibian
import ua.com.andromeda.amphibians.network.AmphibianApiService

interface AmphibianRepository {
    suspend fun findAll(): List<Amphibian>
}

class NetworkAmphibianRepository(
    private val amphibianApiService: AmphibianApiService
) : AmphibianRepository {
    override suspend fun findAll() = amphibianApiService.findAll()
}