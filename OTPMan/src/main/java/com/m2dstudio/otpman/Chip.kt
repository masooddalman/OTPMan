package com.m2dstudio.otpman

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m2dstudio.otpman.dataModel.DataModelChip

enum class ChipState{
    Normal,
    Selected,
    Verified,
    Error
}
@Composable
fun Chip(modifier: Modifier,
         str:String="",
         state: ChipState,
         normal:DataModelChip,
         selected:DataModelChip,
         verified:DataModelChip,
         error:DataModelChip,
         )
{
    val animationDuration = 250

    val animatedBackColor = animateColorAsState(
        when(state){
            ChipState.Normal -> normal.backColor
            ChipState.Selected -> selected.backColor
            ChipState.Verified -> verified.backColor
            ChipState.Error -> error.backColor
        }, label = "backColor",
        animationSpec = tween(durationMillis = animationDuration, easing = EaseInOutCubic)
    )

    val animatedBorderColor = animateColorAsState(
        when(state){
            ChipState.Normal -> normal.borderColor
            ChipState.Selected -> selected.borderColor
            ChipState.Verified -> verified.borderColor
            ChipState.Error -> error.borderColor
        }, label = "borderColor",
        animationSpec = tween(durationMillis = animationDuration, easing = EaseInOutCubic)
    )

    val animatedBorderWidth = animateIntAsState(
        when(state) {
            ChipState.Normal -> normal.borderWidth
            ChipState.Selected -> selected.borderWidth
            ChipState.Verified -> verified.borderWidth
            ChipState.Error -> verified.borderWidth
        }, label = "borderWidth",
        animationSpec = tween(durationMillis = animationDuration, easing = EaseInOutCubic)
    )

    val animatedCornerRadius = animateIntAsState(
        when(state) {
            ChipState.Normal -> normal.cornerRadius
            ChipState.Selected -> selected.cornerRadius
            ChipState.Verified -> verified.cornerRadius
            ChipState.Error -> verified.cornerRadius
        }, label = "cornerRadius",
        animationSpec = tween(durationMillis = animationDuration, easing = EaseInOutCubic)
    )

    val animatedSize = animateIntAsState(
        when(state) {
            ChipState.Normal -> normal.size
            ChipState.Selected -> selected.size
            ChipState.Verified -> verified.size
            ChipState.Error -> error.size
        }, label = "size",
        animationSpec = tween(durationMillis = animationDuration, easing = EaseInOutCubic)
    )

    Box(modifier = modifier
        .size(animatedSize.value.dp)
        .clip(RoundedCornerShape(animatedCornerRadius.value.dp))
        .background(animatedBackColor.value)
        .border(
            width = animatedBorderWidth.value.dp,
            color = animatedBorderColor.value,
            shape = RoundedCornerShape(animatedCornerRadius.value.dp)
        ),
        contentAlignment = Alignment.Center
    ){
        Text(text = str, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp))
    }
}


@Preview
@Composable
fun SampleShapePreview()
{
    Chip(modifier = Modifier,
        str = "5",
        state = ChipState.Normal,
        normal = DataModelChip.normal(),
        selected = DataModelChip.selected(),
        verified = DataModelChip.verified(),
        error = DataModelChip.error()
    )
}


