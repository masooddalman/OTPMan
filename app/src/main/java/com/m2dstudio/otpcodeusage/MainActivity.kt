package com.m2dstudio.otpcodeusage

import android.os.Bundle
import android.util.Log
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
                        OTPMan(modifier = Modifier,
                            viewModel = otpManViewModel,
                            onValueChange = {
                                Log.v("MainActivity", "onValueChanged -> $it")
                            },
                            onComplete = {
                                Log.v("MainActivity", "input code is $it")
                        },
                            onAnimationDone = {
                                Log.v("MainActivity", "animation done $it")
                            })
                        Spacer(modifier = Modifier.padding(16.dp))
                        Button(onClick = {
                            otpManViewModel.updateToSuccessState()
                        }) {
                            Text(text = "send otp code to server and success")
                        }

                        Button(onClick = {
                            otpManViewModel.updateToFailedState()
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
                OTPMan(modifier = Modifier, viewModel = OTPManViewModel(count = 5), onComplete = {})
            }
        }
    }
}

