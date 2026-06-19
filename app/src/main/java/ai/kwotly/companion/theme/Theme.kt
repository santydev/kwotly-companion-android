package ai.kwotly.companion.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Brand-first dark scheme. The WoW tiers drive the three Material accent
// roles; surfaces stay on a near-black zinc ramp for the "dark by default" look.
private val DarkColorScheme = darkColorScheme(
    primary = RareBlue,
    onPrimary = Color.White,
    secondary = EpicPurple,
    onSecondary = Color.White,
    tertiary = LegendaryGold,
    onTertiary = OnTertiaryDark,
    background = BackgroundDark,
    onBackground = OnDark,
    surface = SurfaceDark,
    onSurface = OnDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = CommonZinc,
    outline = CommonZinc,
)

private val LightColorScheme = lightColorScheme(
    primary = RareBlue,
    onPrimary = Color.White,
    secondary = EpicPurple,
    onSecondary = Color.White,
    tertiary = LegendaryGold,
    onTertiary = OnTertiaryDark,
    background = BackgroundLight,
    onBackground = OnLight,
    surface = SurfaceLight,
    onSurface = OnLight,
)

/**
 * Root theme. Defaults to the brand dark scheme.
 *
 * Material You dynamic color is intentionally NOT wired here: it would derive
 * the palette from the device wallpaper and erase the WoW brand identity. The
 * dynamic-color toggle is planned for S2 polish.
 */
@Composable
fun KwotlyCompanionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content,
    )
}