package com.m2dstudio.otpman

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.m2dstudio.otpman.controller.rememberShakeController
import com.m2dstudio.otpman.dataModel.DataModelChip
import com.m2dstudio.otpman.dataModel.ShakeConfig
import com.m2dstudio.otpman.extensions.getFirstColor
import com.m2dstudio.otpman.extensions.getSecondColor
import com.m2dstudio.otpman.modifier.shake
import com.m2dstudio.otpman.utils.getGradientCoordinates

enum class ChipState{
    Normal,
    Selected,
    Verified,
    Error
}
@Composable
fun Chip(modifier: Modifier,
         index:Int=0,
         animationType: AnimationType = AnimationType.Normal,
         str:String="",
         state: ChipState,
         normal:DataModelChip,
         selected:DataModelChip,
         verified:DataModelChip,
         error:DataModelChip,
         mode: ChipMode = ChipMode.Square
         )
{
    val animationDuration = 250
    val animationDelay = if(state == ChipState.Verified || state == ChipState.Error) index*50 else 0
    val shakeConfig = when(state) {
        ChipState.Normal -> ShakeConfig.none
        ChipState.Selected -> ShakeConfig.none
        ChipState.Verified -> if(animationType == AnimationType.Shake) ShakeConfig.success else ShakeConfig.none
        ChipState.Error -> if(animationType == AnimationType.Shake) ShakeConfig.error else ShakeConfig.none
    }
    val shakeController = rememberShakeController().apply {
        this.shake(shakeConfig)
    }

    val bottomPadding = animateIntAsState(
        when(state) {
            ChipState.Normal -> 0
            ChipState.Selected -> 0
            ChipState.Verified -> if(animationType == AnimationType.Normal) 24 else 0
            ChipState.Error -> 0
        }, label = "bottomPadding",
        animationSpec = tween(durationMillis = animationDuration, easing = EaseInOutCubic, delayMillis = animationDelay)
    )

    val animatedBackColor = animateColorAsState(
        when(state){
            ChipState.Normal -> normal.backColor.getFirstColor()
            ChipState.Selected -> selected.backColor.getFirstColor()
            ChipState.Verified -> verified.backColor.getFirstColor()
            ChipState.Error -> error.backColor.getFirstColor()
        }, label = "backColor",
        animationSpec = tween(durationMillis = animationDuration, easing = EaseInOutCubic, delayMillis = animationDelay)
    )
    val animatedBackColor2 = animateColorAsState(
        when(state) {
            ChipState.Normal -> normal.backColor.getSecondColor()
            ChipState.Selected -> selected.backColor.getSecondColor()
            ChipState.Verified -> verified.backColor.getSecondColor()
            ChipState.Error -> error.backColor.getSecondColor()
        }, label = "backColor2",
        animationSpec = tween(durationMillis = animationDuration, easing = EaseInOutCubic, delayMillis = animationDelay)
    )

    val animatedBorderColor = animateColorAsState(
        when(state){
            ChipState.Normal -> normal.borderColor.getFirstColor()
            ChipState.Selected -> selected.borderColor.getFirstColor()
            ChipState.Verified -> verified.borderColor.getFirstColor()
            ChipState.Error -> error.borderColor.getFirstColor()
        }, label = "borderColor",
        animationSpec = tween(durationMillis = animationDuration, easing = EaseInOutCubic, delayMillis = animationDelay)
    )

    val animatedBorderColor2 = animateColorAsState(
        when(state) {
            ChipState.Normal -> normal.borderColor.getSecondColor()
            ChipState.Selected -> selected.borderColor.getSecondColor()
            ChipState.Verified -> verified.borderColor.getSecondColor()
            ChipState.Error -> error.borderColor.getSecondColor()
        }, label = "borderColor2",
        animationSpec = tween(durationMillis = animationDuration, easing = EaseInOutCubic, delayMillis = animationDelay)
    )

    val animatedBorderWidth = animateIntAsState(
        when(state) {
            ChipState.Normal -> normal.borderWidth
            ChipState.Selected -> selected.borderWidth
            ChipState.Verified -> verified.borderWidth
            ChipState.Error -> verified.borderWidth
        }, label = "borderWidth",
        animationSpec = tween(durationMillis = animationDuration, easing = EaseInOutCubic, delayMillis = animationDelay)
    )

    val animatedCornerRadius = animateIntAsState(
        when(state) {
            ChipState.Normal -> normal.cornerRadius
            ChipState.Selected -> selected.cornerRadius
            ChipState.Verified -> verified.cornerRadius
            ChipState.Error -> verified.cornerRadius
        }, label = "cornerRadius",
        animationSpec = tween(durationMillis = animationDuration, easing = EaseInOutCubic, delayMillis = animationDelay)
    )

    val animatedSize = animateIntAsState(
        when(state) {
            ChipState.Normal -> normal.size
            ChipState.Selected -> selected.size
            ChipState.Verified -> verified.size
            ChipState.Error -> error.size
        }, label = "size",
        animationSpec = tween(durationMillis = animationDuration, easing = EaseInOutCubic, delayMillis = animationDelay)
    )

    val textStyle = when(state) {
        ChipState.Normal -> normal.textStyle
        ChipState.Selected -> selected.textStyle
        ChipState.Verified -> verified.textStyle
        ChipState.Error -> error.textStyle
    }

    val angle = when(state) {
        ChipState.Normal -> normal.angle
        ChipState.Selected -> selected.angle
        ChipState.Verified -> verified.angle
        ChipState.Error -> error.angle
    }

    val normalizedAngle: Float = (angle % 360 + 360) % 360
    val angleInRadians: Float = Math.toRadians(normalizedAngle.toDouble()).toFloat()
    val (start, end) = getGradientCoordinates(
        chipSize = animatedSize.value*2,
        normalizedAngle = normalizedAngle,
        angleInRadians = angleInRadians)

    Box(
        modifier=modifier
            .shake(shakeController)
            .padding(bottom = bottomPadding.value.dp),
        contentAlignment = if(mode == ChipMode.Square) Alignment.Center else Alignment.BottomCenter
    ){
        Box(modifier = modifier
            .size(
                animatedSize.value.dp,
                if (mode == ChipMode.Square) animatedSize.value.dp else if (mode == ChipMode.Line) 5.dp else 0.dp
            )
            .clip(RoundedCornerShape(animatedCornerRadius.value.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(animatedBackColor.value, animatedBackColor2.value),
                    start = start,
                    end = end
                )
            )
            .border(
                width = animatedBorderWidth.value.dp,
                brush = Brush.linearGradient(
                    colors = listOf(animatedBorderColor.value, animatedBorderColor2.value),
                    start = start,
                    end = end
                ),
                shape = RoundedCornerShape(animatedCornerRadius.value.dp)
            )
        )
        Text(
            modifier = modifier.shake(shakeController),
            text = str, style = textStyle)
    }
}


@Preview
@Composable
fun SampleShapePreview()
{
    Chip(modifier = Modifier,
        str = "5",
        state = ChipState.Normal,
        normal = DataModelChip.normalGradient(),
        selected = DataModelChip.selected(),
        verified = DataModelChip.verified(),
        error = DataModelChip.error()
    )
}


