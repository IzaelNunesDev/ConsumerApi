package com.rotafacil.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rotafacil.app.domain.model.Route
import com.rotafacil.app.domain.usecase.GetActiveRoutesUseCase
import com.rotafacil.app.domain.usecase.DeleteRouteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouteViewModel @Inject constructor(
    private val getActiveRoutesUseCase: GetActiveRoutesUseCase,
    private val deleteRouteUseCase: DeleteRouteUseCase
) : ViewModel() {

    private val _routeState = MutableStateFlow<RouteState>(RouteState.Initial)
    val routeState: StateFlow<RouteState> = _routeState.asStateFlow()

    init {
        loadActiveRoutes()
    }

    fun loadActiveRoutes() {
        _routeState.value = RouteState.Loading
        
        viewModelScope.launch {
            try {
                val result = getActiveRoutesUseCase()
                result.fold(
                    onSuccess = { routes ->
                        _routeState.value = RouteState.Success(routes)
                    },
                    onFailure = { exception ->
                        _routeState.value = RouteState.Error(exception.message ?: "Erro ao carregar rotas")
                    }
                )
            } catch (e: Exception) {
                _routeState.value = RouteState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun refreshRoutes() {
        loadActiveRoutes()
    }

    fun searchRoutes(text: String) {
        _routeState.value = RouteState.Loading
        viewModelScope.launch {
            try {
                val result = getActiveRoutesUseCase.search(text)
                result.fold(
                    onSuccess = { routes -> _routeState.value = RouteState.Success(routes) },
                    onFailure = { exception -> _routeState.value = RouteState.Error(exception.message ?: "Erro na busca") }
                )
            } catch (e: Exception) {
                _routeState.value = RouteState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun filterRoutes(turno: String?, ativa: Boolean?) {
        _routeState.value = RouteState.Loading
        viewModelScope.launch {
            try {
                val result = getActiveRoutesUseCase.filter(turno, ativa)
                result.fold(
                    onSuccess = { routes -> _routeState.value = RouteState.Success(routes) },
                    onFailure = { exception -> _routeState.value = RouteState.Error(exception.message ?: "Erro no filtro") }
                )
            } catch (e: Exception) {
                _routeState.value = RouteState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun deleteRoute(routeId: String) {
        viewModelScope.launch {
            try {
                val result = deleteRouteUseCase(routeId)
                result.fold(
                    onSuccess = {
                        // Recarregar a lista após deletar
                        loadActiveRoutes()
                    },
                    onFailure = { exception ->
                        _routeState.value = RouteState.Error(exception.message ?: "Erro ao deletar rota")
                    }
                )
            } catch (e: Exception) {
                _routeState.value = RouteState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
}

sealed class RouteState {
    object Initial : RouteState()
    object Loading : RouteState()
    data class Success(val routes: List<Route>) : RouteState()
    data class Error(val message: String) : RouteState()
} 