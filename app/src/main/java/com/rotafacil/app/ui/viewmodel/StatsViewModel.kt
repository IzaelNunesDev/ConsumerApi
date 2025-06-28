package com.rotafacil.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rotafacil.app.data.remote.dto.TripStatsDto
import com.rotafacil.app.data.remote.dto.VehicleStatsDto
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
                val vehicleStatsResult = vehicleRepository.getVehicleStats()
                val tripStatsResult = vehicleRepository.getTripStatsByPeriod()
                
                vehicleStatsResult.fold(
                    onSuccess = { vehicleStats ->
                        tripStatsResult.fold(
                            onSuccess = { tripStats ->
                                _statsState.value = StatsState.Success(
                                    vehicleStats = vehicleStats,
                                    tripStats = tripStats
                                )
                            },
                            onFailure = { exception ->
                                _statsState.value = StatsState.Error(exception.message ?: "Erro ao carregar estatísticas de viagens")
                            }
                        )
                    },
                    onFailure = { exception ->
                        _statsState.value = StatsState.Error(exception.message ?: "Erro ao carregar estatísticas de veículos")
                    }
                )
            } catch (e: Exception) {
                _statsState.value = StatsState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
    
    fun loadTripStatsByPeriod(dataInicio: String?, dataFim: String?) {
        _statsState.value = StatsState.Loading
        
        viewModelScope.launch {
            try {
                val result = vehicleRepository.getTripStatsByPeriod(dataInicio, dataFim)
                result.fold(
                    onSuccess = { tripStats ->
                        // Manter as estatísticas de veículos atuais
                        val currentState = _statsState.value
                        if (currentState is StatsState.Success) {
                            _statsState.value = currentState.copy(tripStats = tripStats)
                        }
                    },
                    onFailure = { exception ->
                        _statsState.value = StatsState.Error(exception.message ?: "Erro ao carregar estatísticas por período")
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
        val vehicleStats: List<VehicleStatsDto>,
        val tripStats: TripStatsDto
    ) : StatsState()
    data class Error(val message: String) : StatsState()
} 