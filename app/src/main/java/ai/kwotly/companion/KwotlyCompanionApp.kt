package ai.kwotly.companion

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application entry point. [HiltAndroidApp] generates the application-level
 * Hilt component that hosts the app's dependency graph; every
 * [dagger.hilt.android.AndroidEntryPoint] resolves against it.
 */
@HiltAndroidApp
class KwotlyCompanionApp : Application()