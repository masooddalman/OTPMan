package com.m2dstudio.otpman.modifier


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import com.m2dstudio.otpman.controller.ShakeController
import kotlin.math.roundToInt

fun Modifier.shake(shakeController: ShakeController) = composed {
    shakeController.shakeConfig?.let { shakeConfig ->
        val shake = remember { Animatable(0f) }
        LaunchedEffect(shakeController.shakeConfig) {
            for (i in 0..shakeConfig.iterations) {
                when (i % 2) {
                    0 -> shake.animateTo(1f, spring(stiffness = shakeConfig.intensity))
                    else -> shake.animateTo(-1f, spring(stiffness = shakeConfig.intensity))
                }
            }
            shake.animateTo(0f)
        }

        this
            .rotate(shake.value * shakeConfig.rotate)
            .graphicsLayer {
                rotationX = shake.value * shakeConfig.rotateX
                rotationY = shake.value * shakeConfig.rotateY
            }
            .scale(
                scaleX = 1f + (shake.value * shakeConfig.scaleX),
                scaleY = 1f + (shake.value * shakeConfig.scaleY),
            )
            .offset {
                IntOffset(
                    (shake.value * shakeConfig.translateX).roundToInt(),
                    (shake.value * shakeConfig.translateY).roundToInt(),
                )
            }
    } ?: this
}