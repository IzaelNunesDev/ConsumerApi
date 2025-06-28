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
class TripDetailViewModel @Inject constructor(
    private val getTripsUseCase: GetTripsUseCase
) : ViewModel() {

    private val _tripDetailState = MutableStateFlow<TripDetailState>(TripDetailState.Initial)
    val tripDetailState: StateFlow<TripDetailState> = _tripDetailState.asStateFlow()

    fun loadTripDetail(viagemId: String) {
        _tripDetailState.value = TripDetailState.Loading
        
        viewModelScope.launch {
            try {
                val result = getTripsUseCase.getTripById(viagemId)
                result.fold(
                    onSuccess = { trip ->
                        _tripDetailState.value = TripDetailState.Success(trip)
                    },
                    onFailure = { exception ->
                        _tripDetailState.value = TripDetailState.Error(exception.message ?: "Erro ao carregar viagem")
                    }
                )
            } catch (e: Exception) {
                _tripDetailState.value = TripDetailState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
}

sealed class TripDetailState {
    object Initial : TripDetailState()
    object Loading : TripDetailState()
    data class Success(val trip: Trip) : TripDetailState()
    data class Error(val message: String) : TripDetailState()
} 