package com.m2dstudio.otpman.dataModel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class DataModelChip(
    val size: Int,
    val backColor: Color,
    val borderColor: Color,
    val borderWidth: Int,
    val cornerRadius: Int,
    val textStyle: TextStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp)
)
{
    companion object{
       fun normal() = DataModelChip(size = 50, backColor = Color.Gray, borderColor = Color.Transparent, borderWidth = 0, cornerRadius = 8)
        fun selected() = DataModelChip(size = 50, backColor = Color.LightGray, borderColor = Color.Cyan, borderWidth = 2, cornerRadius = 16)
        fun verified() = DataModelChip(size = 50, backColor = Color.LightGray, borderColor = Color.Green, borderWidth = 4, cornerRadius = 20)
    }
}
