package com.rotafacil.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rotafacil.app.domain.repository.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {

    private val _statsState = MutableStateFlow<StatsState>(StatsState.Initial)
    val statsState: StateFlow<StatsState> = _statsState.asStateFlow()

    fun loadStats() {
        _statsState.value = StatsState.Loading
        
        viewModelScope.launch {
            try {
                val vehiclesResult = vehicleRepository.getVehicles()
                vehiclesResult.fold(
                    onSuccess = { vehicles ->
                        _statsState.value = StatsState.Success(
                            totalVehicles = vehicles.size,
                            activeVehicles = vehicles.size,
                            maintenanceVehicles = 0
                        )
                    },
                    onFailure = { exception ->
                        _statsState.value = StatsState.Error(exception.message ?: "Erro ao carregar estatísticas")
                    }
                )
            } catch (e: Exception) {
                _statsState.value = StatsState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
}

sealed class StatsState {
    object Initial : StatsState()
    object Loading : StatsState()
    data class Success(
        val totalVehicles: Int,
        val activeVehicles: Int,
        val maintenanceVehicles: Int
    ) : StatsState()
    data class Error(val message: String) : StatsState()
} 