package ai.kwotly.companion.di

import javax.inject.Qualifier

/** Qualifies the application-lifetime [kotlinx.coroutines.CoroutineScope]. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope