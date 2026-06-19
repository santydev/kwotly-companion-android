package ai.kwotly.companion.data.repository

import ai.kwotly.companion.data.local.AuthDataStore
import ai.kwotly.companion.data.remote.KwotlyApi
import ai.kwotly.companion.data.remote.toDisplayableError
import ai.kwotly.companion.data.remote.dto.LoginRequest
import ai.kwotly.companion.data.remote.dto.toDomain
import ai.kwotly.companion.domain.model.AuthSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: KwotlyApi,
    private val authDataStore: AuthDataStore,
    private val json: Json,
) : AuthRepository {

    override val authToken: Flow<String?> = authDataStore.token

    override val isLoggedIn: Flow<Boolean> =
        authDataStore.token.map { token -> !token.isNullOrBlank() }

    override suspend fun login(email: String, password: String): Result<AuthSession> =
        runCatching {
            val response = api.login(LoginRequest(email.trim(), password))
            authDataStore.saveToken(response.token)
            AuthSession(token = response.token, user = response.user.toDomain())
        }.recoverCatching { cause -> throw cause.toDisplayableError(json, "Sign-in failed") }

    override suspend fun logout() = authDataStore.clear()
}