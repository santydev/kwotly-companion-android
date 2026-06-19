package ai.kwotly.companion.data.remote

import ai.kwotly.companion.data.remote.dto.LoginRequest
import ai.kwotly.companion.data.remote.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit contract for the Kwotly backend (app.kwotly.ai).
 *
 * Suspend functions return the decoded body on 2xx and throw
 * [retrofit2.HttpException] on any other status — repositories map that
 * into a domain-level result. Authenticated endpoints (quotes) land in J2-J3.
 */
interface KwotlyApi {

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse
}
