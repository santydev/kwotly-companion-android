package ai.kwotly.companion.domain.model

/**
 * Authenticated Kwotly user. Mirrors the subset of fields the backend
 * returns on login; the full profile is fetched lazily via /api/auth/me.
 */
data class User(
    val id: String,
    val email: String,
    val userType: UserType,
)

/**
 * Account role. Drives which app shell the user lands in. [UNKNOWN] guards
 * against the backend introducing a new role the app doesn't recognise yet.
 */
enum class UserType {
    HOMEOWNER,
    CONTRACTOR,
    UNKNOWN;

    companion object {
        fun fromApi(raw: String): UserType = when (raw.lowercase()) {
            "homeowner" -> HOMEOWNER
            "contractor" -> CONTRACTOR
            else -> UNKNOWN
        }
    }
}
