package com.m2dstudio.otpman

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m2dstudio.otpman.dataModel.DataModelChip
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class OTPState{
    Idle, Success, Failed
}

enum class AnimationType{
    Normal, Shake
}

@Composable
fun OTPMan(modifier: Modifier, count: Int, space:Int = 8,
           keyboardType: KeyboardType = KeyboardType.Number,
           animationType: AnimationType = AnimationType.Normal,
           normal:DataModelChip = DataModelChip.normalGradient(),
           selected:DataModelChip = DataModelChip.selectedGradient(),
           verified:DataModelChip = DataModelChip.verifiedGradient(),
           error:DataModelChip = DataModelChip.errorGradient(),
           showRippleEffect:Boolean = false,
           otpState: OTPState = OTPState.Idle,
           onValueChange:(String)->Unit = {},
           onComplete:(String)->Unit,
           onAnimationDone:(Boolean)-> Unit = {}
           )
{
    val focusManager = LocalFocusManager.current
    var value by remember {
        mutableStateOf("")
    }
    val focus by remember {
        mutableStateOf(FocusRequester())
    }

    val textData = remember {
        mutableStateListOf<String>()
    }.apply {
        for(i in 0 until count)
        {
            add("")
        }
    }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(otpState != OTPState.Idle) {
        coroutineScope.launch {
            if(otpState == OTPState.Success)
            {
                delay(if(animationType == AnimationType.Shake) 1000 else (count * 50.toLong()) + 250)
                onAnimationDone(true)
            }
            if(otpState == OTPState.Failed)
            {
                delay(if(animationType == AnimationType.Shake) 1000 else (count * 50.toLong()) + 250)
                onAnimationDone(false)
            }
        }
    }

    val interactionSource = remember { MutableInteractionSource() }

    return Box(modifier = Modifier, contentAlignment = Alignment.Center){
        TextField(
            modifier = Modifier
                .focusRequester(focus),
            value = value, onValueChange = {
                if (it.length <= count)
                {
                    value = it

                    for(i in 0 until count)
                    {
                        if(i < value.length)
                        {
                            textData[i] = value[i].toString()
                        }
                        else
                        {
                            textData[i] = ""
                        }
                        onValueChange(value)
                    }
                }
                if (it.length >= count)
                {
                    focusManager.clearFocus()
                    onComplete(value)
                }
        },
            singleLine = true,
            textStyle = TextStyle(fontSize = 1.sp, color = Color.Transparent),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(space.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items(count) { index ->
                Chip(modifier = Modifier,
                    index = index,
                    animationType = animationType,
                    str = textData[index],
                    state = calculateOtpState(otpState, textData[index]),
                    normal = normal,
                    selected = selected,
                    verified = verified,
                    error = error
                )
            }
        }
        Box(
            modifier
                .fillMaxWidth()
                .height(normal.size.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = if (showRippleEffect) LocalIndication.current else null
                ) {
                    focusManager.clearFocus()
                    focus.requestFocus()
                }
            )
    }
}

fun calculateOtpState(otpState: OTPState, text:String): ChipState {
    return if(otpState == OTPState.Idle)
    {
        if(text.isEmpty()) ChipState.Normal else ChipState.Selected
    }
    else
    {
        if(otpState == OTPState.Success)
        {
            ChipState.Verified
        }
        else
        {
            ChipState.Error
        }
    }
}


@Preview
@Composable
fun OTPManPreview()
{
    OTPMan(modifier = Modifier, count = 4, onComplete = {

    })
}