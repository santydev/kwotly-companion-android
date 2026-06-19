package ai.kwotly.companion.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Persists the JWT bearer token across launches. Wraps the raw
 * [DataStore] in a typed surface so callers never touch preference keys.
 */
@Singleton
class AuthDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    /** Emits the stored token, or null when signed out. */
    val token: Flow<String?> = dataStore.data
        // A corrupt-read on disk surfaces as IOException — recover to an
        // empty state (signed out) rather than crashing the auth flow.
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { prefs -> prefs[KEY_TOKEN] }

    suspend fun saveToken(token: String) {
        dataStore.edit { prefs -> prefs[KEY_TOKEN] = token }
    }

    suspend fun clear() {
        dataStore.edit { prefs -> prefs.remove(KEY_TOKEN) }
    }

    private companion object {
        val KEY_TOKEN = stringPreferencesKey("jwt_token")
    }
}
