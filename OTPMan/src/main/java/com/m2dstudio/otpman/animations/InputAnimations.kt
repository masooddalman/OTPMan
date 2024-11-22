package com.m2dstudio.otpman.animations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith

class InputAnimations {
    companion object{
        val slideInBottomSlideOutTop : (AnimatedContentTransitionScope<String>.() -> ContentTransform) = {
            slideInVertically{ it } togetherWith  slideOutVertically { -it }
        }
        val slideInBottomSlideOutBottom : (AnimatedContentTransitionScope<String>.() -> ContentTransform) = {
            slideInVertically{ it } togetherWith  slideOutVertically { it }
        }
        val fadeInFadeOut : (AnimatedContentTransitionScope<String>.() -> ContentTransform) = {
            fadeIn() togetherWith  fadeOut()
        }
        val fadeInSlideOutTop : (AnimatedContentTransitionScope<String>.() -> ContentTransform) = {
            fadeIn() togetherWith  slideOutVertically { -it }
        }
        val fadeInSlideOutBottom : (AnimatedContentTransitionScope<String>.() -> ContentTransform) = {
            fadeIn() togetherWith  slideOutVertically { it }
        }
        val slideInBottomFadeOut : (AnimatedContentTransitionScope<String>.() -> ContentTransform) = {
            slideInVertically{ it } togetherWith  fadeOut()
        }
        val slideInTopFadeOut : (AnimatedContentTransitionScope<String>.() -> ContentTransform) = {
            slideInVertically{ -it } togetherWith  fadeOut()
        }
    }
}