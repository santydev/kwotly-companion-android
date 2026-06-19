package ai.kwotly.companion.domain.model

/**
 * Result of a successful authentication: the bearer [token] used for
 * subsequent authenticated requests, paired with the signed-in [user].
 */
data class AuthSession(
    val token: String,
    val user: User,
)
