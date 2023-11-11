package ua.com.andromeda.amphibians.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import ua.com.andromeda.amphibians.MyApplication
import ua.com.andromeda.amphibians.data.AmphibianRepository
import ua.com.andromeda.amphibians.network.Amphibian
import java.io.IOException

sealed interface AmphibiansUiState {
    data class SUCCESS(val amphibians: List<Amphibian>) : AmphibiansUiState
    object ERROR : AmphibiansUiState
    object LOADING : AmphibiansUiState
}

class AmphibiansViewModel(
    private val amphibianRepository: AmphibianRepository
) : ViewModel() {
    var uiState: AmphibiansUiState by mutableStateOf(AmphibiansUiState.LOADING)
        private set

    init {
        findAll()
    }

    private fun findAll() {
        viewModelScope.launch {
            uiState = try {
                AmphibiansUiState.SUCCESS(amphibianRepository.findAll())
            } catch (e: IOException) {
                AmphibiansUiState.ERROR
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                val amphibianRepository = application.appContainer.amphibianRepository
                AmphibiansViewModel(amphibianRepository)
            }
        }
    }
}
