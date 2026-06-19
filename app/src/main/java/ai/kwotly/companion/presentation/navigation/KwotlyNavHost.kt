package ai.kwotly.companion.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ai.kwotly.companion.presentation.auth.LoginRoute
import ai.kwotly.companion.presentation.quotes.QuoteDetailRoute
import ai.kwotly.companion.presentation.quotes.QuotesListRoute

/** Top-level route identifiers. */
private object Routes {
    const val LOGIN = "login"
    const val QUOTES = "quotes"
    const val QUOTE_ARG = "quoteId"
    const val QUOTE_DETAIL = "quotes/{$QUOTE_ARG}"

    fun quoteDetail(id: String) = "quotes/$id"
}

/**
 * Auth/main routing. Starts at login; a successful sign-in replaces the back
 * stack so Back doesn't return to the form. Quote rows push a detail route
 * carrying the quote id. Auto-login from a persisted token is a later pass.
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
                    navController.navigate(Routes.QUOTES) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.QUOTES) {
            QuotesListRoute(
                onQuoteClick = { id -> navController.navigate(Routes.quoteDetail(id)) },
            )
        }
        composable(
            route = Routes.QUOTE_DETAIL,
            arguments = listOf(navArgument(Routes.QUOTE_ARG) { type = NavType.StringType }),
        ) {
            QuoteDetailRoute(onBack = { navController.popBackStack() })
        }
    }
}
