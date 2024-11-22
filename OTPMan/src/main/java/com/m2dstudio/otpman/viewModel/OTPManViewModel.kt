package com.m2dstudio.otpman.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.m2dstudio.otpman.AnimationType
import com.m2dstudio.otpman.ChipMode
import com.m2dstudio.otpman.OTPState
import com.m2dstudio.otpman.dataModel.DataModelChip

class OTPManViewModel(
    val count: Int,
    val keyboardType: KeyboardType = KeyboardType.Number,
    val animationType: AnimationType = AnimationType.Normal,
    val mode: ChipMode = ChipMode.Square,
    val normal: DataModelChip = DataModelChip.normalGradient(),
    val selected: DataModelChip = DataModelChip.selectedGradient(),
    val verified: DataModelChip = DataModelChip.verifiedGradient(),
    val error: DataModelChip = DataModelChip.errorGradient()
) : ViewModel() {

    var value = mutableStateOf(TextFieldValue(""))
        private set
    var textData = mutableStateListOf<String>()
        private set
    var state: OTPState by mutableStateOf(OTPState.Idle)
        private set

    init {
        for (i in 0 until count) {
            textData.add("")
        }
    }

    fun getValue(): TextFieldValue {
        return value.value
    }

    private fun updateOTPState(otpState: OTPState) {
        state = otpState
    }

    fun makeItSuccess()
    {
        if(getValue().text.length == count) {
            updateOTPState(OTPState.Success)
        }
    }

    fun makeItFailed()
    {
        if(getValue().text.length == count) {
            updateOTPState(OTPState.Failed)
        }
    }

    fun updateValue(str: String) {
        value.value = TextFieldValue(str, selection = TextRange(str.length))
    }

    fun updateValue(
        newValue: TextFieldValue,
        bigger: (String) -> Unit,
        lessEqual: (String) -> Unit
    ) {
        if (newValue.text.length <= count) {
            value.value = newValue
            for (i in 0 until count) {
                if (i < value.value.text.length) {
                    textData[i] = value.value.text[i].toString()
                } else {
                    textData[i] = ""
                }
            }
            updateOTPState(OTPState.Idle)
            lessEqual.invoke(value.value.text)
        }
        if (newValue.text.length >= count) {
            bigger.invoke(value.value.text)
        }
    }

    fun refreshTextData(str: String) {
        textData.clear()
        for (element in str) {
            textData.add(element.toString())
        }
    }
}