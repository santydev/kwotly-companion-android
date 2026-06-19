package ai.kwotly.companion.theme

import androidx.compose.ui.graphics.Color

// ---- WoW item-tier palette — Kwotly brand identity ----
// Each tier maps to a Material role; see Theme.kt for the wiring.
val RareBlue = Color(0xFF0070DD)      // primary   — actions, primary buttons
val EpicPurple = Color(0xFF9333EA)    // secondary — accents
val LegendaryGold = Color(0xFFF0B82E) // tertiary  — money moments (totals, deposits, signed)
val CommonZinc = Color(0xFFA1A1AA)    // neutral   — muted text, outlines

// ---- Dark surfaces (default theme) — near-black zinc ramp ----
val BackgroundDark = Color(0xFF0A0A0B)
val SurfaceDark = Color(0xFF18181B)
val SurfaceVariantDark = Color(0xFF27272A)
val OnDark = Color(0xFFFAFAFA)
val OnTertiaryDark = Color(0xFF1A1300) // dark text on gold for contrast

// ---- Light surfaces (optional Material You fallback, S2) ----
val BackgroundLight = Color(0xFFFFFFFF)
val SurfaceLight = Color(0xFFFAFAFA)
val OnLight = Color(0xFF18181B)