package com.m2dstudio.otpman.extensions

import androidx.compose.ui.graphics.Color

fun List<Color>.getSecondaryColor(): Color {
    return this.getOrNull(1) ?: this.firstOrNull()
        ?: throw IllegalStateException("Color array cannot be empty, it should at least has 1 color")
}

fun List<Color>.getFistColor(): Color {
    return this.firstOrNull()
        ?: throw IllegalStateException("Color array cannot be empty, it should at least has 1 color")
}