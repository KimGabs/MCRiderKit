package com.example.mcriderkit.ui.local

import android.content.Context
import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppContext = staticCompositionLocalOf<Context> {
    error("No app context provided")
}
