package com.rotafacil.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rotafacil.app.domain.model.Route
import com.rotafacil.app.domain.usecase.GetActiveRoutesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouteDetailViewModel @Inject constructor(
    private val getActiveRoutesUseCase: GetActiveRoutesUseCase
) : ViewModel() {

    private val _routeDetailState = MutableStateFlow<RouteDetailState>(RouteDetailState.Initial)
    val routeDetailState: StateFlow<RouteDetailState> = _routeDetailState.asStateFlow()

    fun loadRouteDetail(rotaId: String) {
        _routeDetailState.value = RouteDetailState.Loading
        
        viewModelScope.launch {
            try {
                val result = getActiveRoutesUseCase()
                result.fold(
                    onSuccess = { routes ->
                        val route = routes.find { it.id == rotaId }
                        if (route != null) {
                            _routeDetailState.value = RouteDetailState.Success(route)
                        } else {
                            _routeDetailState.value = RouteDetailState.Error("Rota não encontrada")
                        }
                    },
                    onFailure = { exception ->
                        _routeDetailState.value = RouteDetailState.Error(exception.message ?: "Erro ao carregar rota")
                    }
                )
            } catch (e: Exception) {
                _routeDetailState.value = RouteDetailState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
}

sealed class RouteDetailState {
    object Initial : RouteDetailState()
    object Loading : RouteDetailState()
    data class Success(val route: Route) : RouteDetailState()
    data class Error(val message: String) : RouteDetailState()
} 