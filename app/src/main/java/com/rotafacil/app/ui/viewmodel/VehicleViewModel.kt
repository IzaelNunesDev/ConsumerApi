package com.rotafacil.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rotafacil.app.domain.model.Vehicle
import com.rotafacil.app.domain.usecase.GetVehiclesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val getVehiclesUseCase: GetVehiclesUseCase
) : ViewModel() {

    private val _vehicleState = MutableStateFlow<VehicleState>(VehicleState.Initial)
    val vehicleState: StateFlow<VehicleState> = _vehicleState.asStateFlow()

    init {
        loadVehicles()
    }

    fun loadVehicles() {
        _vehicleState.value = VehicleState.Loading
        
        viewModelScope.launch {
            try {
                val result = getVehiclesUseCase()
                result.fold(
                    onSuccess = { vehicles ->
                        _vehicleState.value = VehicleState.Success(vehicles)
                    },
                    onFailure = { exception ->
                        _vehicleState.value = VehicleState.Error(exception.message ?: "Erro ao carregar veículos")
                    }
                )
            } catch (e: Exception) {
                _vehicleState.value = VehicleState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun refreshVehicles() {
        loadVehicles()
    }

    fun searchVehicles(text: String) {
        _vehicleState.value = VehicleState.Loading
        viewModelScope.launch {
            try {
                val result = getVehiclesUseCase.search(text)
                result.fold(
                    onSuccess = { vehicles -> _vehicleState.value = VehicleState.Success(vehicles) },
                    onFailure = { exception -> _vehicleState.value = VehicleState.Error(exception.message ?: "Erro na busca") }
                )
            } catch (e: Exception) {
                _vehicleState.value = VehicleState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun filterVehicles(statusManutencao: String?, adaptadoPcd: Boolean?) {
        _vehicleState.value = VehicleState.Loading
        viewModelScope.launch {
            try {
                val result = getVehiclesUseCase.filter(statusManutencao, adaptadoPcd)
                result.fold(
                    onSuccess = { vehicles -> _vehicleState.value = VehicleState.Success(vehicles) },
                    onFailure = { exception -> _vehicleState.value = VehicleState.Error(exception.message ?: "Erro no filtro") }
                )
            } catch (e: Exception) {
                _vehicleState.value = VehicleState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
}

sealed class VehicleState {
    object Initial : VehicleState()
    object Loading : VehicleState()
    data class Success(val vehicles: List<Vehicle>) : VehicleState()
    data class Error(val message: String) : VehicleState()
} 