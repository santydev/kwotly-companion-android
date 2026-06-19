package ai.kwotly.companion.data.remote.dto

import ai.kwotly.companion.domain.model.User
import ai.kwotly.companion.domain.model.UserType
import kotlinx.serialization.Serializable

/** Body for `POST /api/auth/login`. */
@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)

/**
 * Success payload from `POST /api/auth/login`.
 * Shape: `{ success, token, user: { id, email, userType } }`.
 */
@Serializable
data class LoginResponse(
    val success: Boolean,
    val token: String,
    val user: UserDto,
)

@Serializable
data class UserDto(
    val id: String,
    val email: String,
    val userType: String,
)

/** Error envelope the backend returns on any non-2xx: `{ error: "..." }`. */
@Serializable
data class ApiErrorDto(
    val error: String,
)

fun UserDto.toDomain(): User = User(
    id = id,
    email = email,
    userType = UserType.fromApi(userType),
)
