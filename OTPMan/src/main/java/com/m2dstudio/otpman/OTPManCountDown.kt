package com.m2dstudio.otpman

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

enum class CountDownMode{
    Seconds, Minutes, MinutesThenSeconds
}

enum class CounterAnimationType {
    None, Fade, Slide, SlideInverse
}

@Composable
fun OTPManCountDown(
    modifier: Modifier=Modifier,
    secondsInFuture: Int = 120,
    mode: CountDownMode = CountDownMode.Minutes,
    animationType: CounterAnimationType = CounterAnimationType.Fade,
    prefixContent:String? = "",
    postFixContent:String? = "",
    textStyle: TextStyle = TextStyle(),
    onFinished: (() -> Unit)? = null,
    onTick: (() -> Unit)? = null,
    onResend: (() -> Unit)? = null,
    resendContent: @Composable (RowScope.() -> Unit)? = {
        Icon(
            imageVector = Icons.Outlined.Refresh,
            contentDescription = null
        )
        Text(text = "Resend")
    }
) {
    var timeLeft by rememberSaveable { mutableIntStateOf(secondsInFuture) }
    var isRunning by remember { mutableStateOf(true) }

    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            isRunning=true
            delay(1000L) // Delay for 1 second
            timeLeft -= 1
            onTick?.invoke()
        } else {
            isRunning = false
            onFinished?.invoke()
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        if(isRunning || resendContent == null) {
            var oldTimeLeft by remember {
                mutableIntStateOf(timeLeft)
            }
            SideEffect {
                oldTimeLeft = timeLeft
            }
            Row {
                Text(text = "$prefixContent ", style = textStyle)
                if(animationType == CounterAnimationType.None) {
                    Text(text = makeString(mode, timeLeft), style = textStyle)
                }
                else {
                    val timeString = makeString(mode, timeLeft)
                    val oldTimeString = makeString(mode, oldTimeLeft)
                    for (i in timeString.indices) {
                        val oldChar = oldTimeString.getOrNull(i)
                        val newChar = timeString.getOrNull(i)
                        val char = if (oldChar == newChar) {
                            oldChar
                        } else {
                            newChar
                        }
                        if(animationType == CounterAnimationType.Fade) {
                            AnimatedContent(
                                targetState = char,
                                label = "animated time string"
                            ) {
                                Text(text = it.toString(), style = textStyle)
                            }
                        }
                        else if (animationType == CounterAnimationType.Slide) {
                            AnimatedContent(
                                targetState = char,
                                transitionSpec = {
                                    slideInVertically { it } togetherWith  slideOutVertically { -it }
                                },
                                label = "animated time string"
                            ) {
                                Text(text = it.toString(), style = textStyle)
                            }
                        }
                        else if(animationType == CounterAnimationType.SlideInverse) {
                            AnimatedContent(
                                targetState = char,
                                transitionSpec = {
                                    slideInVertically { -it } togetherWith  slideOutVertically { it }
                                },
                                label = "animated time string"
                            ) {
                                Text(text = it.toString(), style = textStyle)
                            }
                        }
                    }
                }
                Text(text = " $postFixContent", style = textStyle)
            }

        }
        else {
            Row(content = resendContent,
                modifier = Modifier.clickable {
                    timeLeft = secondsInFuture
                    onResend?.invoke()
                })
        }
    }
}


fun makeString(mode:CountDownMode, time:Int) : String {
    return when (mode) {
        CountDownMode.Seconds -> makeSecondsString(time.seconds)
        CountDownMode.Minutes -> makeMinutesString(time.seconds)
        CountDownMode.MinutesThenSeconds -> {
            if(time <= 60) {
                makeSecondsString(time.seconds)
            } else {
                makeMinutesString(time.seconds)
            }
        }
    }
}

private fun makeSecondsString(duration: Duration) : String {
    return "${duration.inWholeSeconds.toInt()}"
}

private fun makeMinutesString(duration: Duration) : String {
    val h = duration.inWholeHours
    val m = (duration - h.hours).inWholeMinutes
    val s = (duration - h.hours - m.minutes).inWholeSeconds

    val hours: String = if(h.toInt() == 0) { "" }
    else if(h.toInt() < 10) { "0$h:" }
    else { "${h.toInt()}:" }

    val minutes: String = if(m.toInt() < 10) { "0$m:" }
    else { "$m:" }

    val secs: String = if(s.toInt() < 10) { "0$s" }
    else { "$s" }

    return "$hours$minutes$secs"
}

@Composable
@Preview
fun TextCountDownPreview(){
    OTPManCountDown(resendContent = {
        Row {
            Icon(
                imageVector = Icons.Outlined.Refresh,
                contentDescription = null
            )
            Text(text = "Resend")
        }
    })
}