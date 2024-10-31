package com.m2dstudio.otpman.extensions

import androidx.compose.ui.graphics.Color

fun List<Color>.getSecondColor(): Color {
    return this.getOrNull(1) ?: this.firstOrNull()
        ?: throw IllegalStateException("Color list cannot be empty, At least one color is required.")
}

fun List<Color>.getFirstColor(): Color {
    return this.firstOrNull()
        ?: throw IllegalStateException("Color list cannot be empty, At least one color is required.")
}