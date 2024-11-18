# OTPMan

![Kotlin](https://img.shields.io/badge/language-Kotlin-blue) ![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI%20Framework-green) ![Android](https://img.shields.io/badge/Platform-Android-orange)

## Overview
**OTPMan** is an all-in-one solution which automatically reads and inserts OTP (One-Time Password) codes received via SMS. This library functions without requiring SMS permissions, leveraging the Android OTP API for secure and easy OTP handling within your app’s UI. It automatically manages animation, state and data on every situation with customizable colors, shapes and animation.

## Features
- **Automatic OTP Handling**: Reads and inserts OTP codes directly into the app UI.
- **Automatic animation and user input handling**: Just put the composable in your screen and you're good to go
- **Automatic change device configuration handling**: Don't worry about screen rotation, device theme changing, etc.
- **Generate hash key**: You don’t need to put up with annoying CLI or open source Python tools to generate hash key, OTPMan can generate the hash key inside Android Studio by simply running your app, even in emulator
- **Permission-Free**: Automatic SMS Verification with the [SMS Retriever API](https://developers.google.com/identity/sms-retriever/overview).
- **Jetpack Compose Compatible**: Designed to work seamlessly with Jetpack Compose, Android's modern UI toolkit.

## Installation
1: add it to `settings.gradle.kts`
```gradle
maven { url = uri("https://jitpack.io") }
```
2: add it to your app's `build.gradle.kts` dependencies
```gradle
implementation("com.github.masooddalman:OTPMan:0.0.1")
```
3: sync it 😁
## Usage (simple)
1: create an instance of `OTPManViewModel` in an Activity or Fragment and define how many digits your OTP code has
```kotlin
class MainActivity : ComponentActivity() {
```
```kotlin
private val otpManViewModel by viewModels<OTPManViewModel>{  
  object : ViewModelProvider.Factory {  
        override fun <T : ViewModel> create(modelClass: Class<T>): T {  
            return OTPManViewModel(  
                count = 5  
  ) as T  
  }  
    }  
}
```
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
```
2: add OTPMan composable in the UI and pass the ViewModel to it
```kotlin
OTPMan(modifier = Modifier,  
    viewModel = otpManViewModel,  
    onComplete = {  
  Log.v("tag", "input code is $it")  
})
```
3: that's it, **OnComplete callback** will trigger when the code completely filled, either by the user or auto-reading from the SMS

## Automatically reading the OTP code
To let the library automatically read the OTP code, you have to generate app hash key for your application, it is a unique an 11-character string based on your app package name and signing info.
please follow steps below:

 1. **Generating app hash key**
 Write the code below in the `onCreate` function of the first Activity of the application
 ```Kotlin
 generateAppHashKey(this)
```
 2. **Search for the generated hash key in the Logcat** 
*to find the hash key easier, add this to logcat filter*

![logcatFilter](https://github.com/masooddalman/OTPMan/blob/main/assets/logcat_filter.png)
```
otpman
```
| generated hash key for DEBUG | generated hash key for RELEASE |
|--------|--------|
| ![debug](https://github.com/masooddalman/OTPMan/blob/main/assets/sample_debug_logcat.png) | ![release](https://github.com/masooddalman/OTPMan/blob/main/assets/sample_release_logcat.png) |

 3. **Removing the code**

	**ATTENTION 1** : you can test your app with the DEBUG hash key, but don't forget to use the RELEASE one on production

	**ATTENTION 2** : Don't forget to remove the code above from your App before building a release, it's only for generating the hash key

 4. **dding the Hash key in your OTP SMS**
  [Construct a verification message](https://developers.google.com/identity/sms-retriever/verify#1_construct_a_verification_message)
-   Be no longer than 140 bytes
-   Contain a one-time code that the client sends back to your server to complete the verification flow 
-   Include an 11-character hash string that identifies your app

Otherwise, the contents of the verification message can be whatever you choose. It is helpful to create a message from which you can easily extract the one-time code later on. For example, a valid verification message might look like the following:
examples:
```
welcome to my app
verification code is 35467
bpqiJ6uwmGi
```
or
```
Your ExampleApp code is: 123456

FA+9qCX9VSu
```
![autoReadOTP](https://github.com/masooddalman/OTPMan/blob/main/assets/otpmanAutoReadOTPCode.gif)

## Success and Failed animations
The library animation for showing **failed** and **success** result.
after sending the OTP code to your server, you can call one of these based on it.
```
otpManViewModel.makeItFailed() 
```
or
```
otpManViewModel.makeItSuccess()
```
you can also change the animation type in the **OTPManViewModel construction** like below(default is Normal):
```
animationType = AnimationType.Normal
 ```
 or
 ```
animationType = AnimationType.Shake
```
it automatically removes the success or failed UI by any changes in the inserted code
| normal | shake |
|--------|--------|
| ![normal animation](https://github.com/masooddalman/OTPMan/blob/main/assets/normal_animation.gif) | ![shake animation](https://github.com/masooddalman/OTPMan/blob/main/assets/shake_animation.gif) |

## Styling
you can change style (background and border) of the fields in **the OTPManViewModel construction** using `DataModelChip` class.
```
DataModelChip(  
    val size: Int,  
    val backColor: List<Color>,  
    val borderColor: List<Color>,  
    val angle:Float = 0f,  
    val borderWidth: Int,  
    val cornerRadius: Int,  
    val textStyle: TextStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp)  
)
```

 - `size`: size of each input field.
 -  `backColor`: input fields background color (list of one color means solid color and list of tow
   colors means gradient)
 - `borderColor`: input fields border color (list
   of one color means solid color and list of tow colors means gradient)
    - `angle`: angle of background and border gradient
 - `borderWidth`: input fields border width `cornerRadius`: input fields corner radius
 -  `textStyle`: input field text stye

**the OTPManViewModel construction** has 4 different inputs like below:
```
OTPManViewModel(  
    count = 5,  
    animationType = AnimationType.Shake,  
    normal = DataModelChip(...),  
    selected = DataModelChip(...),  
    verified = DataModelChip(...),  
    error = DataModelChip(...)  
)
```

 - `normal` : when the input field is empty
 - `selected` : when the input field is filled
 - `verified` : input field style in success mode
 - `error` : input field style in failed mode

**Bonus:**
The library comes with a class named Gradients which has different type of gradients ready to use.
```
 Gradients.gradeGray
```
or if you need to add opacity to these gradients, use it like below (e.g. 50% opacity) :
```
Gradients.gradeGray(0.5f)
```

## OTPMan composable

**OTPMan composable** itself has some configurations like below:

 - `space` : space between each input fields (default = 8dp)
 - `showRippleEffect` : show ripple effect on user touch (default =
   false)
 - `onValueChange‍` : it triggers on every changes in fields (either
   adding or removing a character)
 - `onAnimationDone`: it triggers after success or failed animation was
   done
 - `onComplete` : it triggers when the code completely filled, either by
   the user or auto-reading from the SMS


## Count-Down
Every screen with verification code input needs a count-down timer.
you can use **OTPManCountDown composable** in the app's UI like below:

    OTPManCountDown()
the configuration will be:

 - `secondsInFuture`: timer in seconds (default : 120 seconds)
 - `mode`: count-down 3 different formats (default: Minute)

  | Seconds | Minutes | MinutesThenSeconds |
|--------|--------|--------|
| ![seconds mode](https://github.com/masooddalman/OTPMan/blob/main/assets/sec.gif) | ![minutes mode](https://github.com/masooddalman/OTPMan/blob/main/assets/min.gif) | ![minutesThenSeconds mode](https://github.com/masooddalman/OTPMan/blob/main/assets/minsec.gif) |

 - `prefixContent`: a String to show **before** the count-down
 - `postFixContent`: a String to show **after** the count-down
 - `textStyle`: style of the count-down
 - `onFinished`: it triggers when the count-down is finished
 - `onTick`: it triggers on each second
 - `onResend`: it triggers when the user interacts with the Resend button
 - `resendContent`: the content of resend button
