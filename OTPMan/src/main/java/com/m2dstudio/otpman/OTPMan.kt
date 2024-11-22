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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.m2dstudio.otpman.viewModel.OTPManViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class OTPState{
    Idle, Success, Failed
}

enum class AnimationType{
    Normal, Shake
}

enum class ChipMode {
    Square, Line, None
}

@Composable
fun OTPMan(modifier: Modifier,
           space:Int = 8,
           showRippleEffect:Boolean = false,
           viewModel: OTPManViewModel,
           onValueChange:(String)->Unit = {},
           onComplete:(String)->Unit,
           onAnimationDone:(Boolean)-> Unit = {}
           )
{
    val focusManager = LocalFocusManager.current

    val focus by remember {
        mutableStateOf(FocusRequester())
    }

    // Broadcast
    SmsListener { intent ->
        if (intent?.action == SmsRetriever.SMS_RETRIEVED_ACTION) {

            val extras = intent.extras
            val status = extras?.get(SmsRetriever.EXTRA_STATUS) as? Status
            if (status?.statusCode == CommonStatusCodes.SUCCESS) {
                val message = extras.getString(SmsRetriever.EXTRA_SMS_MESSAGE, "")
                val otpReceived = Regex("[0-9]{${viewModel.count}}").find(message)?.value
                otpReceived?.let {
                    viewModel.updateValue(otpReceived)
                    viewModel.refreshTextData(otpReceived)
                    onValueChange(otpReceived)
                    onComplete(otpReceived)
                }
            }
        }
    }
    //UI
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(viewModel.state != OTPState.Idle) {
        coroutineScope.launch {
            if(viewModel.state == OTPState.Success)
            {
                delay(if(viewModel.animationType == AnimationType.Shake) 1000 else (viewModel.count * 50.toLong()) + 250)
                onAnimationDone(true)
            }
            if(viewModel.state == OTPState.Failed)
            {
                delay(if(viewModel.animationType == AnimationType.Shake) 1000 else (viewModel.count * 50.toLong()) + 250)
                onAnimationDone(false)
            }
        }
    }

    val interactionSource = remember { MutableInteractionSource() }

    return Box(modifier = Modifier, contentAlignment = Alignment.Center){
        TextField(
            modifier = Modifier
                .focusRequester(focus),
            value = viewModel.getValue(), onValueChange = {
                viewModel.updateValue(it, lessEqual = { newVal ->
                    onValueChange.invoke(newVal)
                }, bigger = { inputCode ->
                    onComplete(inputCode)
                    focusManager.clearFocus()
                })
        },
            singleLine = true,
            textStyle = TextStyle(fontSize = 0.sp, color = Color.Transparent),
            keyboardOptions = KeyboardOptions(keyboardType = viewModel.keyboardType),
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
            items(viewModel.count) { index ->
                Chip(modifier = Modifier,
                    index = index,
                    animationType = viewModel.animationType,
                    str = viewModel.textData[index],
                    state = calculateOtpState(viewModel.state, viewModel.textData[index]),
                    normal = viewModel.normal,
                    selected = viewModel.selected,
                    verified = viewModel.verified,
                    error = viewModel.error,
                    mode = viewModel.mode
                )
            }
        }
        Box(
            modifier
                .fillMaxWidth()
                .height(viewModel.normal.size.dp)
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
    OTPMan(modifier = Modifier, viewModel = OTPManViewModel(count = 5), onComplete = {})
}