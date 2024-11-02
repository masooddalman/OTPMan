package com.m2dstudio.otpman.gradients

import androidx.compose.ui.graphics.Color

class Gradients {
    companion object{
        fun transparent() = listOf(Color.Transparent, Color.Transparent)
        fun gradeGray(opacity:Float = 1f) = listOf(Color(0xFFbdc3c7).copy(alpha = opacity),Color(0xFF2c3e50).copy(alpha = opacity))
        fun piggyPink(opacity:Float = 1f) = listOf(Color(0xffee9ca7).copy(alpha = opacity),Color(0xffffdde1).copy(alpha = opacity))
        fun coolBlues(opacity:Float = 1f) = listOf(Color(0xff2193b0).copy(alpha = opacity),Color(0xff6dd5ed).copy(alpha = opacity))
        fun eveningSunshine(opacity:Float = 1f) = listOf(Color(0xffb92b27).copy(alpha = opacity),Color(0xff1565C0).copy(alpha = opacity))
        fun darkOcean(opacity:Float = 1f) = listOf(Color(0xff373B44).copy(alpha = opacity),Color(0xff4286f4).copy(alpha = opacity))
        fun yoda(opacity:Float = 1f) = listOf(Color(0xffFF0099).copy(alpha = opacity),Color(0xff493240).copy(alpha = opacity))
        fun amin(opacity:Float = 1f) = listOf(Color(0xff8E2DE2).copy(alpha = opacity),Color(0xff4A00E0).copy(alpha = opacity))
        fun harvey(opacity:Float = 1f) = listOf(Color(0xff1f4037).copy(alpha = opacity),Color(0xff99f2c8).copy(alpha = opacity))
        fun neuromancer(opacity:Float = 1f) = listOf(Color(0xfff953c6).copy(alpha = opacity),Color(0xffb91d73).copy(alpha = opacity))
        fun flair(opacity:Float = 1f) = listOf(Color(0xfff12711).copy(alpha = opacity),Color(0xfff5af19).copy(alpha = opacity))
    }
}