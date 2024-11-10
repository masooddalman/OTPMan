package com.m2dstudio.otpman.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Arrays
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

const val TAG = "OTPMan"
private fun print(message:String,error:Boolean=false){
    if(error)
    {
        Log.e(TAG,"❌$message")
    }
    else
    {
        Log.d(TAG,"✅$message")
    }
}
fun getGradientCoordinates(chipSize:Int, normalizedAngle: Float, angleInRadians:Float): Pair<Offset, Offset> {
    val size = Size(chipSize*2f, chipSize*2f)
    val diagonal = sqrt(size.width.pow(2) + size.height.pow(2))
    val angleBetweenDiagonalAndWidth = acos(size.width / diagonal)
    val angleBetweenDiagonalAndGradientLine =
        if ((normalizedAngle > 90 && normalizedAngle < 180)
            || (normalizedAngle > 270 && normalizedAngle < 360)
        ) {
            PI.toFloat() - angleInRadians - angleBetweenDiagonalAndWidth
        } else {
            angleInRadians - angleBetweenDiagonalAndWidth
        }
    val halfGradientLine = abs(cos(angleBetweenDiagonalAndGradientLine) * diagonal) / 2

    val horizontalOffset = halfGradientLine * cos(angleInRadians)
    val verticalOffset = halfGradientLine * sin(angleInRadians)

    //Add bounds checking for final coordinates
    //The final coordinate calculations should ensure the points remain within the chip bounds to prevent gradient artifacts
    fun clampOffset(offset: Offset, bounds: Size): Offset = Offset(
        x = offset.x.coerceIn(0f, bounds.width),
        y = offset.y.coerceIn(0f, bounds.height)
    )

    val start = clampOffset(size.center + Offset(-horizontalOffset, verticalOffset), size)
    val end = clampOffset(size.center + Offset(horizontalOffset, -verticalOffset), size)

    return start to end
}

private const val HASH_TYPE = "SHA-256"
const val NUM_HASHED_BYTES: Int = 9
const val NUM_BASE64_CHAR: Int = 11
fun generateAppHashKey(context: Context) {
    val appCodes = ArrayList<String>()
    try {

        // Get all package signatures for the current package
        val packageName = context.packageName
        val packageManager = context.packageManager
        val signatures = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_SIGNATURES
        ).signatures

        // For each signature create a compatible hash
        for (signature in signatures) {
            val hash = hash(packageName, signature.toCharsString())
            if (hash != null) {
                appCodes.add(String.format("%s", hash))
            }
        }
    } catch (e: PackageManager.NameNotFoundException) {
        print("Unable to find package to obtain hash.\n$e", true)
    }
    val isDebuggable = 0 != context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    print("================== App hash key ==================", isDebuggable)
    print("=", isDebuggable)
    print("=", isDebuggable)
    print("= package name = ${context.packageName}", isDebuggable)
    print("=", isDebuggable)
    print("=      👉👉👉👉${appCodes.first()}👈👈👈👈   ", isDebuggable)
    print("=", isDebuggable)
    print("=", isDebuggable)
    if(isDebuggable)
    {
        print("= be careful, this hash key only works for debug version, don't use it in production", true)
        print("= please run the project in release mode to generate release hash key", true)
    }
    print("= usage : https://github.com/masooddalman/OTPMan", isDebuggable)
    print("================== App hash key ==================", isDebuggable)
}

private fun hash(packageName: String, signature: String): String? {
    val appInfo = "$packageName $signature"
    try {
        val messageDigest = MessageDigest.getInstance(HASH_TYPE)
        messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
        var hashSignature = messageDigest.digest()

        // truncated into NUM_HASHED_BYTES
        hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES)
        // encode into Base64
        var base64Hash =
            Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
        base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR)

        return base64Hash
    } catch (e: NoSuchAlgorithmException) {
        print("hash:NoSuchAlgorithm\n$e",true)

    }
    return null
}