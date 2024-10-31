package com.m2dstudio.otpman.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

fun getGradientCoordinates(chipSize:Int, normalizedAngle: Float, angleInRadians:Float): Pair<Offset, Offset> {
    val size = Size(chipSize*2f, chipSize*2f)
    val diagonal = sqrt(size.width.pow(2) + size.height.pow(2))
    val angleBetweenDiagonalAndWidth = acos(size.width / diagonal)
    val angleBetweenDiagonalAndGradientLine =
        if ((normalizedAngle > 90 && normalizedAngle < 180)
            || (normalizedAngle > 270 && normalizedAngle < 360)
        ) {
            PI.toFloat() - angleInRadians - angleBetweenDiagonalAndWidth
        } else {
            angleInRadians - angleBetweenDiagonalAndWidth
        }
    val halfGradientLine = abs(cos(angleBetweenDiagonalAndGradientLine) * diagonal) / 2

    val horizontalOffset = halfGradientLine * cos(angleInRadians)
    val verticalOffset = halfGradientLine * sin(angleInRadians)

    val start = size.center + Offset(-horizontalOffset, verticalOffset)
    val end = size.center + Offset(horizontalOffset, -verticalOffset)

    return start to end
}
