package com.nithin.weightpicker.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

sealed class LineType(val lineLength : Dp,val lineColor : Color) {

    data class NormalLineType(val length: Dp,val color: Color) : LineType(length,color)

    data class FiveStepLineType(val length: Dp,val color: Color) : LineType(length,color)

    data class TenStepLineType(val length: Dp,val color: Color) : LineType(length,color)

}