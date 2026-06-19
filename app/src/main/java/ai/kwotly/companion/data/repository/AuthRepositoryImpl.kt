package ai.kwotly.companion.data.repository

import ai.kwotly.companion.data.local.AuthDataStore
import ai.kwotly.companion.data.remote.KwotlyApi
import ai.kwotly.companion.data.remote.KwotlyApiException
import ai.kwotly.companion.data.remote.dto.ApiErrorDto
import ai.kwotly.companion.data.remote.dto.LoginRequest
import ai.kwotly.companion.data.remote.dto.toDomain
import ai.kwotly.companion.domain.model.AuthSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
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
        }.recoverCatching { cause -> throw cause.toDisplayable() }

    override suspend fun logout() = authDataStore.clear()

    /** Maps transport/HTTP failures into a [KwotlyApiException] with a UI-ready message. */
    private fun Throwable.toDisplayable(): Throwable = when (this) {
        is HttpException -> {
            val backendMessage = response()?.errorBody()?.string()
                ?.let { body -> runCatching { json.decodeFromString<ApiErrorDto>(body).error }.getOrNull() }
            KwotlyApiException(backendMessage ?: "Sign-in failed (HTTP ${code()}).")
        }
        is IOException -> KwotlyApiException("Network error — check your connection.")
        else -> this
    }
}