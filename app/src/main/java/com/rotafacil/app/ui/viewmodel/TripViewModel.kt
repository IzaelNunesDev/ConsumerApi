package com.rotafacil.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rotafacil.app.data.local.DataStoreManager
import com.rotafacil.app.domain.model.Trip
import com.rotafacil.app.domain.usecase.GetTripsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val getTripsUseCase: GetTripsUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _tripState = MutableStateFlow<TripState>(TripState.Initial)
    val tripState: StateFlow<TripState> = _tripState.asStateFlow()

    init {
        loadTrips()
    }

    fun loadTrips() {
        _tripState.value = TripState.Loading
        
        viewModelScope.launch {
            val studentId = dataStoreManager.userId.firstOrNull()
            if (studentId == null) {
                _tripState.value = TripState.Error("ID do usuário não encontrado. Faça login novamente.")
                return@launch
            }

            try {
                val result = getTripsUseCase.getTripsByStudent(studentId)
                result.fold(
                    onSuccess = { trips ->
                        _tripState.value = TripState.Success(trips)
                    },
                    onFailure = { exception ->
                        val errorMessage = when {
                            exception.message?.contains("500") == true -> 
                                "Erro no servidor. Tente novamente mais tarde."
                            exception.message?.contains("404") == true -> 
                                "Nenhuma viagem encontrada para este usuário."
                            exception.message?.contains("401") == true -> 
                                "Sessão expirada. Faça login novamente."
                            exception.message?.contains("403") == true -> 
                                "Acesso negado. Verifique suas permissões."
                            exception.message?.contains("timeout") == true -> 
                                "Tempo limite excedido. Verifique sua conexão."
                            else -> exception.message ?: "Erro ao carregar viagens"
                        }
                        _tripState.value = TripState.Error(errorMessage)
                    }
                )
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("timeout") == true -> 
                        "Tempo limite excedido. Verifique sua conexão."
                    e.message?.contains("UnknownHostException") == true -> 
                        "Sem conexão com a internet."
                    else -> e.message ?: "Erro desconhecido"
                }
                _tripState.value = TripState.Error(errorMessage)
            }
        }
    }

    fun refreshTrips() {
        loadTrips()
    }

    fun filterTrips(status: String?, dataInicio: String?, dataFim: String?) {
        _tripState.value = TripState.Loading
        viewModelScope.launch {
            try {
                val result = getTripsUseCase.filter(status, dataInicio, dataFim)
                result.fold(
                    onSuccess = { trips -> _tripState.value = TripState.Success(trips) },
                    onFailure = { exception -> 
                        val errorMessage = when {
                            exception.message?.contains("500") == true -> 
                                "Erro no servidor ao filtrar viagens."
                            exception.message?.contains("timeout") == true -> 
                                "Tempo limite excedido ao filtrar."
                            else -> exception.message ?: "Erro no filtro"
                        }
                        _tripState.value = TripState.Error(errorMessage)
                    }
                )
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("timeout") == true -> 
                        "Tempo limite excedido ao filtrar."
                    else -> e.message ?: "Erro desconhecido no filtro"
                }
                _tripState.value = TripState.Error(errorMessage)
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