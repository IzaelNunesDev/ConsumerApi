package com.rotafacil.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rotafacil.app.domain.model.Trip
import com.rotafacil.app.domain.usecase.GetTripsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val getTripsUseCase: GetTripsUseCase
) : ViewModel() {

    private val _tripState = MutableStateFlow<TripState>(TripState.Initial)
    val tripState: StateFlow<TripState> = _tripState.asStateFlow()

    fun loadTrips(studentId: String) {
        _tripState.value = TripState.Loading
        
        viewModelScope.launch {
            try {
                val result = getTripsUseCase(studentId)
                result.fold(
                    onSuccess = { trips ->
                        _tripState.value = TripState.Success(trips)
                    },
                    onFailure = { exception ->
                        _tripState.value = TripState.Error(exception.message ?: "Erro ao carregar viagens")
                    }
                )
            } catch (e: Exception) {
                _tripState.value = TripState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun refreshTrips(studentId: String) {
        loadTrips(studentId)
    }

    fun filterTrips(status: String?, dataInicio: String?, dataFim: String?) {
        _tripState.value = TripState.Loading
        viewModelScope.launch {
            try {
                val result = getTripsUseCase.filter(status, dataInicio, dataFim)
                result.fold(
                    onSuccess = { trips -> _tripState.value = TripState.Success(trips) },
                    onFailure = { exception -> _tripState.value = TripState.Error(exception.message ?: "Erro no filtro") }
                )
            } catch (e: Exception) {
                _tripState.value = TripState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
}

sealed class TripState {
    object Initial : TripState()
    object Loading : TripState()
    data class Success(val trips: List<Trip>) : TripState()
    data class Error(val message: String) : TripState()
} 