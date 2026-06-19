package ai.kwotly.companion.presentation.auth

import ai.kwotly.companion.data.repository.AuthRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events.asSharedFlow()

    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value).clearedError() }

    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value).clearedError() }

    fun onSubmit() {
        val current = _uiState.value
        if (!current.canSubmit) return

        viewModelScope.launch {
            _uiState.update { it.copy(status = LoginStatus.Loading) }
            authRepository.login(current.email, current.password)
                .onSuccess {
                    _uiState.update { it.copy(status = LoginStatus.Idle) }
                    _events.emit(LoginEvent.NavigateToHome)
                }
                .onFailure { cause ->
                    _uiState.update {
                        it.copy(status = LoginStatus.Error(cause.message ?: "Sign-in failed."))
                    }
                }
        }
    }

    /** Drops a lingering error once the user edits the form again. */
    private fun LoginUiState.clearedError(): LoginUiState =
        if (status is LoginStatus.Error) copy(status = LoginStatus.Idle) else this
}