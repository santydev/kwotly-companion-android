package ai.kwotly.companion.data.remote

import ai.kwotly.companion.data.local.AuthDataStore
import ai.kwotly.companion.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Keeps the latest JWT in memory so [AuthInterceptor] can read it
 * synchronously on OkHttp's background thread — no `runBlocking` in the
 * request chain. A single app-scoped collector mirrors the DataStore token
 * flow into a volatile field.
 */
@Singleton
class TokenProvider @Inject constructor(
    authDataStore: AuthDataStore,
    @ApplicationScope scope: CoroutineScope,
) {
    @Volatile
    var current: String? = null
        private set

    init {
        scope.launch {
            authDataStore.token.collect { token -> current = token }
        }
    }
}