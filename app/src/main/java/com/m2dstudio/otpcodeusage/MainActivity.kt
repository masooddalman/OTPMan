package com.m2dstudio.otpcodeusage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m2dstudio.otpcodeusage.ui.theme.OTPCodeUsageTheme
import com.m2dstudio.otpman.OTPMan
import com.m2dstudio.otpman.OTPState
import com.m2dstudio.otpman.viewModel.OTPManViewModel

class MainActivity : ComponentActivity() {
    private val otpManViewModel by viewModels<OTPManViewModel>{
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return OTPManViewModel(
                    count = 5
                ) as T
            }
        }
    }

    private var otpState = mutableStateOf(OTPState.Idle)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            OTPCodeUsageTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,) {
                        OTPMan(modifier = Modifier, count = 5,
                            animationType = AnimationType.Shake,
                            otpState = otpState.value,
                            onValueChange = {
                                otpState.value = OTPState.Idle
                            },
                            onComplete = {
                            println("input code is $it")
                        },
                            onAnimationDone = {
                                println("animation done $it")
                            })
                        Spacer(modifier = Modifier.padding(16.dp))
                        Button(onClick = {
                            otpState.value = OTPState.Success
                        }) {
                            Text(text = "send otp code to server and success")
                        }

                        Button(onClick = {
                            otpState.value = OTPState.Failed
                        }) {
                            Text(text = "send otp code to server and failed")
                        }

                    }

                }
            }
        }
    }
}

@Preview
@Composable
fun MainActivityPreview() {
    OTPCodeUsageTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPassing ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPassing)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,) {
                OTPMan(modifier = Modifier, count = 5, onComplete = {})
            }
        }
    }
}

