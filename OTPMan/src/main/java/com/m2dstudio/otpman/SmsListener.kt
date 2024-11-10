package com.m2dstudio.otpman

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.android.gms.tasks.Task
import com.m2dstudio.otpman.utils.TAG


@Composable
fun SmsListener(
    onSystemEvent: (intent: Intent?) -> Unit
) {
    val context = LocalContext.current

    val currentOnSystemEvent by rememberUpdatedState( onSystemEvent )
    val client: SmsRetrieverClient = SmsRetriever.getClient(context)
    val task: Task<Void> = client.startSmsRetriever()

    task.addOnSuccessListener {

        // Successfully started retriever, expect broadcast intent
        Log.v(TAG, "Successfully started retriever, expect broadcast intent")
    }

    task.addOnFailureListener {
        // Failed to start retriever, inspect Exception for more details
        Log.v(TAG, "Failed to start retriever, inspect Exception for more details")

    }

    DisposableEffect(context, SmsRetriever.SMS_RETRIEVED_ACTION){

        val intentFilter = IntentFilter( SmsRetriever.SMS_RETRIEVED_ACTION )

        val receiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.v(TAG, "SMSReceiver something received")
                currentOnSystemEvent( intent )
            }
        }

        // works on all android version
        ContextCompat.registerReceiver(
            context,
            receiver,
            intentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }
}