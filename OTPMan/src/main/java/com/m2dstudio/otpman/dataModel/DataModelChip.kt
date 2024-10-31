package com.m2dstudio.otpman.dataModel

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class DataModelChip(
    val size: Int,
    val backColor: List<Color>,
    val borderColor: List<Color>,
    val angle:Float = 0f,
    val borderWidth: Int,
    val cornerRadius: Int,
    val textStyle: TextStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp)
)
{
    companion object{
       fun normal() = DataModelChip(size = 50, backColor = listOf(Color.Gray), borderColor = listOf(Color.Transparent), borderWidth = 0, cornerRadius = 8)
        fun selected() = DataModelChip(size = 50, backColor = listOf(Color.LightGray), borderColor = listOf(Color.Cyan), borderWidth = 2, cornerRadius = 16)
        fun verified() = DataModelChip(size = 50, backColor = listOf(Color.LightGray), borderColor = listOf(Color.Green), borderWidth = 4, cornerRadius = 20)
        fun error() = DataModelChip(size = 50, backColor = listOf(Color.Red.copy(alpha = 0.25f)), borderColor = listOf(Color.Red), borderWidth = 4, cornerRadius = 16)

        fun normalGradient() = DataModelChip(
            size = 50,
            backColor = listOf(Color.Gray, Color.LightGray),
            borderColor = listOf(Color.Transparent, Color.Transparent),
            borderWidth = 0, cornerRadius = 8)

        fun selectedGradient() = DataModelChip(
            size = 50,
            backColor = listOf(Color.LightGray, Color.Cyan),
            borderColor = listOf(Color.Cyan, Color.Cyan),
            borderWidth = 2, cornerRadius = 16)

        fun verifiedGradient() = DataModelChip(
            size = 50,
            backColor = listOf(Color.LightGray, Color.Green),
            borderColor = listOf(Color.Green, Color.Green),
            borderWidth = 4, cornerRadius = 20)

        fun errorGradient() = DataModelChip(
            size = 50,
            backColor = listOf(Color.Red.copy(alpha = 0.25f), Color.Red),
            borderColor = listOf(Color.Red, Color.Red),
            borderWidth = 4, cornerRadius = 16)

    }
}
