package com.nithin.weightpicker.presentation

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nithin.weightpicker.data.ClockStyle


import java.time.Clock
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Clock(
    modifier: Modifier,
    clockStyle : ClockStyle = ClockStyle()
) {

    var center by remember {
        mutableStateOf(Offset.Zero)
    }

    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    val radius = clockStyle.radius
    val lineLength = clockStyle.lineLength


    Canvas(modifier = modifier){
        center = this.center

        circleCenter = Offset(x = center.x , center.y)

        println("Circle Center $circleCenter")

        drawContext.canvas.nativeCanvas.apply {

            drawCircle(
                circleCenter.x,
                circleCenter.y,
                clockStyle.radius.toPx(),
                Paint().apply {
                    color = android.graphics.Color.WHITE
                    strokeWidth = 5f
                    style = Paint.Style.STROKE
                    setShadowLayer(
                        60f,
                        0f,
                        0f,
                        Color.argb(50,0,0,0)
                    )
                }

            )

        }

        for (theta in 0..360 step 30){ // same as i = i + 30

            val angle = Math.toRadians(theta.toDouble())

            val startOffset = Offset(
                x = (circleCenter.x + (radius.toPx() * cos(angle))).toFloat(),
                y = (circleCenter.y) + (radius.toPx() * sin(angle)).toFloat()
            )

            val endOffset = Offset(
                x = (circleCenter.x + (radius.toPx() - lineLength.toPx()) * cos(angle)).toFloat(),
                y = (circleCenter.y + (radius.toPx() - lineLength.toPx()) * sin(angle)).toFloat()
            )

            drawLine(
                color = _root_ide_package_.androidx.compose.ui.graphics.Color.Green,
                start = startOffset,
                end = endOffset,

            )

        }




    }

}

@Preview(showBackground = true)
@Composable
private fun PreviewClock(

) {

    Box(modifier = Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Color.Black)) {
        Clock(
            modifier = Modifier.fillMaxWidth().align(Alignment.Center)
        )
    }

}