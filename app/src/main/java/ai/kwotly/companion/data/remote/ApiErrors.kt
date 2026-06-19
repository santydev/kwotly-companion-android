package ai.kwotly.companion.data.remote

import ai.kwotly.companion.data.remote.dto.ApiErrorDto
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

/**
 * Maps a transport/HTTP failure into a [KwotlyApiException] whose message is
 * safe to show the user — the backend's `{ error }` envelope when present,
 * otherwise a [fallback]. Shared by every repository so error handling stays
 * consistent in one place.
 */
internal fun Throwable.toDisplayableError(
    json: Json,
    fallback: String = "Something went wrong.",
): Throwable = when (this) {
    is HttpException -> {
        val backendMessage = response()?.errorBody()?.string()
            ?.let { body -> runCatching { json.decodeFromString<ApiErrorDto>(body).error }.getOrNull() }
        KwotlyApiException(backendMessage ?: "$fallback (HTTP ${code()}).")
    }
    is IOException -> KwotlyApiException("Network error — check your connection.")
    else -> this
}