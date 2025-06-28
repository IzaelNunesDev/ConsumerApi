package com.rotafacil.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rotafacil.app.domain.model.Route
import com.rotafacil.app.domain.model.StopPoint
import com.rotafacil.app.domain.usecase.CreateRouteUseCase
import com.rotafacil.app.domain.usecase.UpdateRouteUseCase
import com.rotafacil.app.domain.usecase.GetActiveRoutesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouteFormViewModel @Inject constructor(
    private val createRouteUseCase: CreateRouteUseCase,
    private val updateRouteUseCase: UpdateRouteUseCase,
    private val getActiveRoutesUseCase: GetActiveRoutesUseCase
) : ViewModel() {

    private val _formState = MutableStateFlow<RouteFormState>(RouteFormState.Success(RouteForm()))
    val formState: StateFlow<RouteFormState> = _formState.asStateFlow()

    fun updateNome(nome: String) {
        val currentForm = (formState.value as? RouteFormState.Success)?.form ?: RouteForm()
        val updatedForm = currentForm.copy(
            nome = nome,
            nomeError = if (nome.isBlank()) "Nome é obrigatório" else null
        )
        _formState.value = RouteFormState.Success(updatedForm)
    }

    fun updateTurno(turno: String) {
        val currentForm = (formState.value as? RouteFormState.Success)?.form ?: RouteForm()
        val updatedForm = currentForm.copy(turno = turno)
        _formState.value = RouteFormState.Success(updatedForm)
    }

    fun updateIsAtiva(isAtiva: Boolean) {
        val currentForm = (formState.value as? RouteFormState.Success)?.form ?: RouteForm()
        val updatedForm = currentForm.copy(isAtiva = isAtiva)
        _formState.value = RouteFormState.Success(updatedForm)
    }

    fun addStopPoint() {
        val currentForm = (formState.value as? RouteFormState.Success)?.form ?: RouteForm()
        val newStopPoint = StopPoint(
            id = "",
            nome = "",
            ordem = currentForm.pontosDeParada.size + 1,
            latitude = 0.0,
            longitude = 0.0,
            horarioEstimado = null
        )
        val updatedForm = currentForm.copy(
            pontosDeParada = currentForm.pontosDeParada + newStopPoint
        )
        _formState.value = RouteFormState.Success(updatedForm)
    }

    fun updateStopPoint(index: Int, stopPoint: StopPoint) {
        val currentForm = (formState.value as? RouteFormState.Success)?.form ?: RouteForm()
        val updatedStopPoints = currentForm.pontosDeParada.toMutableList()
        updatedStopPoints[index] = stopPoint
        val updatedForm = currentForm.copy(pontosDeParada = updatedStopPoints)
        _formState.value = RouteFormState.Success(updatedForm)
    }

    fun removeStopPoint(index: Int) {
        val currentForm = (formState.value as? RouteFormState.Success)?.form ?: RouteForm()
        val updatedStopPoints = currentForm.pontosDeParada.toMutableList()
        updatedStopPoints.removeAt(index)
        // Reordenar os pontos
        updatedStopPoints.forEachIndexed { i, stopPoint ->
            updatedStopPoints[i] = stopPoint.copy(ordem = i + 1)
        }
        val updatedForm = currentForm.copy(pontosDeParada = updatedStopPoints)
        _formState.value = RouteFormState.Success(updatedForm)
    }

    fun loadRouteForEdit(rotaId: String) {
        _formState.value = RouteFormState.Loading
        
        viewModelScope.launch {
            try {
                val result = getActiveRoutesUseCase()
                result.fold(
                    onSuccess = { routes ->
                        val route = routes.find { it.id == rotaId }
                        if (route != null) {
                            val form = RouteForm(
                                id = route.id,
                                nome = route.nome,
                                turno = route.turno,
                                isAtiva = route.isAtiva,
                                pontosDeParada = route.pontosDeParada,
                                motoristaId = route.motoristaId,
                                veiculoId = route.veiculoId
                            )
                            _formState.value = RouteFormState.Success(form)
                        } else {
                            _formState.value = RouteFormState.Error("Rota não encontrada")
                        }
                    },
                    onFailure = { exception ->
                        _formState.value = RouteFormState.Error(exception.message ?: "Erro ao carregar rota")
                    }
                )
            } catch (e: Exception) {
                _formState.value = RouteFormState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun saveRoute() {
        val currentForm = (formState.value as? RouteFormState.Success)?.form ?: return
        
        if (!currentForm.isValid) {
            return
        }

        _formState.value = RouteFormState.Loading
        
        viewModelScope.launch {
            try {
                val route = Route(
                    id = currentForm.id,
                    nome = currentForm.nome,
                    turno = currentForm.turno,
                    isAtiva = currentForm.isAtiva,
                    pontosDeParada = currentForm.pontosDeParada,
                    motoristaId = currentForm.motoristaId,
                    veiculoId = currentForm.veiculoId
                )

                val result = if (currentForm.id.isBlank()) {
                    createRouteUseCase(route)
                } else {
                    updateRouteUseCase(route)
                }

                result.fold(
                    onSuccess = {
                        _formState.value = RouteFormState.Saved
                    },
                    onFailure = { exception ->
                        _formState.value = RouteFormState.Error(exception.message ?: "Erro ao salvar rota")
                    }
                )
            } catch (e: Exception) {
                _formState.value = RouteFormState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
}

data class RouteForm(
    val id: String = "",
    val nome: String = "",
    val turno: String = "Manhã",
    val isAtiva: Boolean = true,
    val pontosDeParada: List<StopPoint> = emptyList(),
    val motoristaId: String? = null,
    val veiculoId: String? = null,
    val nomeError: String? = null
) {
    val isValid: Boolean
        get() = nome.isNotBlank() && nomeError == null
}

sealed class RouteFormState {
    object Loading : RouteFormState()
    data class Success(val form: RouteForm) : RouteFormState()
    object Saved : RouteFormState()
    data class Error(val message: String) : RouteFormState()
} 