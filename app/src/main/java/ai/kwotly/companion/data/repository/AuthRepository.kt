package ai.kwotly.companion.data.repository

import ai.kwotly.companion.domain.model.AuthSession
import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for authentication state and credentials.
 *
 * [login] performs the network call and persists the token on success;
 * [authToken] / [isLoggedIn] expose the persisted session reactively so the
 * UI can route between the auth and main graphs without imperative checks.
 */
interface AuthRepository {

    val authToken: Flow<String?>

    val isLoggedIn: Flow<Boolean>

    /** Authenticates and persists the session. Failure carries a displayable message. */
    suspend fun login(email: String, password: String): Result<AuthSession>

    suspend fun logout()
}
