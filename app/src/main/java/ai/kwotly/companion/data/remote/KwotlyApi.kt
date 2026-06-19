package ai.kwotly.companion.data.remote

import ai.kwotly.companion.data.remote.dto.LoginRequest
import ai.kwotly.companion.data.remote.dto.LoginResponse
import ai.kwotly.companion.data.remote.dto.QuoteDetailResponse
import ai.kwotly.companion.data.remote.dto.QuotesListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    /** Authenticated contractor's quotes, newest first. */
    @GET("api/quotes/list")
    suspend fun getQuotes(
        @Query("status") status: String? = null,
        @Query("limit") limit: Int? = null,
    ): QuotesListResponse

    @GET("api/quotes/{id}")
    suspend fun getQuote(@Path("id") id: String): QuoteDetailResponse
}
