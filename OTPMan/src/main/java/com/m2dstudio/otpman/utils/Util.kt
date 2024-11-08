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

    //Add bounds checking for final coordinates
    //The final coordinate calculations should ensure the points remain within the chip bounds to prevent gradient artifacts
    fun clampOffset(offset: Offset, bounds: Size): Offset = Offset(
        x = offset.x.coerceIn(0f, bounds.width),
        y = offset.y.coerceIn(0f, bounds.height)
    )

    val start = clampOffset(size.center + Offset(-horizontalOffset, verticalOffset), size)
    val end = clampOffset(size.center + Offset(horizontalOffset, -verticalOffset), size)

    return start to end
}
