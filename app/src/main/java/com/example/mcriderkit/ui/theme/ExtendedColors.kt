package com.example.mcriderkit.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.compositionLocalOf

// This will hold your ExtendedColorScheme at runtime
val LocalExtendedColors = staticCompositionLocalOf {
    extendedLight // default fallback
}

// Optional helper to access it like MaterialTheme
object ExtendedTheme {
    val colors: ExtendedColorScheme
        @Composable
        get() = LocalExtendedColors.current
}
