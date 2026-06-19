package ai.kwotly.companion.presentation.auth

/**
 * Screen state for login. The form fields live alongside a sealed [status]
 * so text survives transient transitions (loading/error) — a fully sealed
 * UiState would drop the user's input on every state change.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val status: LoginStatus = LoginStatus.Idle,
) {
    val canSubmit: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && status != LoginStatus.Loading
}

sealed interface LoginStatus {
    data object Idle : LoginStatus
    data object Loading : LoginStatus
    data class Error(val message: String) : LoginStatus
}

/** One-shot navigation effects, emitted via SharedFlow (never replayed on rotation). */
sealed interface LoginEvent {
    data object NavigateToHome : LoginEvent
}