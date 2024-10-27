package com.m2dstudio.otpman

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

enum class OTPState{
    Idle, Success, Failed
}

@Composable
fun OTPMan(modifier: Modifier, count: Int, space:Int = 8,
           keyboardType: KeyboardType = KeyboardType.Number,
           normal:DataModelChip = DataModelChip.normal(),
           selected:DataModelChip = DataModelChip.selected(),
           verified:DataModelChip = DataModelChip.verified(),
           onComplete:(String)->Unit
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
                    str = textData[index],
                    state = if(textData[index].isEmpty()) ChipState.Normal else ChipState.Selected,
                    normal = normal,
                    selected = selected
                )
            }
        }
        Box(
            modifier
                .fillMaxWidth()
                .height(normal.size.dp)
                .clickable {
                    focusManager.clearFocus()
                    focus.requestFocus()
                }
            )
    }
}



@Preview
@Composable
fun OTPManPreview()
{
    OTPMan(modifier = Modifier, count = 4, onComplete = {

    })
}