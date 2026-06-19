package ai.kwotly.companion.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ai.kwotly.companion.presentation.auth.LoginRoute
import ai.kwotly.companion.presentation.home.HomeScreen

/** Top-level route identifiers. Type-safe nav args land with the quotes graph. */
private object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
}

/**
 * Minimal auth/main routing. Starts at login; a successful sign-in replaces
 * the back stack so Back doesn't return to the form. Auto-login from a
 * persisted token (AuthRepository.isLoggedIn) is wired in a later pass.
 */
@Composable
fun KwotlyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
        modifier = modifier,
    ) {
        composable(Routes.LOGIN) {
            LoginRoute(
                onLoggedIn = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.HOME) {
            HomeScreen()
        }
    }
}