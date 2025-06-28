package com.rotafacil.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rotafacil.app.data.remote.dto.LoginResponseDto
import com.rotafacil.app.domain.model.User
import com.rotafacil.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            authRepository.isLoggedIn().collect { isLoggedIn ->
                _isLoggedIn.value = isLoggedIn
            }
        }
    }

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        
        viewModelScope.launch {
            try {
                val result = authRepository.login(email, password)
                result.fold(
                    onSuccess = { response ->
                        _authState.value = AuthState.Success(response)
                    },
                    onFailure = { exception ->
                        _authState.value = AuthState.Error(exception.message ?: "Erro no login")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun registerStudent(
        name: String,
        email: String,
        password: String,
        registration: String,
        phone: String?
    ) {
        _authState.value = AuthState.Loading
        
        viewModelScope.launch {
            try {
                val result = authRepository.registerStudent(name, email, password, registration, phone)
                result.fold(
                    onSuccess = { response ->
                        _authState.value = AuthState.Success(response)
                    },
                    onFailure = { exception ->
                        _authState.value = AuthState.Error(exception.message ?: "Erro no registro")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _authState.value = AuthState.Initial
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            try {
                val result = authRepository.getCurrentUser()
                result.fold(
                    onSuccess = { user ->
                        _authState.value = AuthState.UserLoaded(user)
                    },
                    onFailure = { exception ->
                        _authState.value = AuthState.Error(exception.message ?: "Erro ao carregar usuário")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Initial
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Success(val response: LoginResponseDto) : AuthState()
    data class UserLoaded(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
} 