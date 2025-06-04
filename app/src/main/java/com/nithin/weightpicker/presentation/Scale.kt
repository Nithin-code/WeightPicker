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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nithin.weightpicker.data.ScaleStyle
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Scale(
    modifier: Modifier = Modifier,
    scaleStyle: ScaleStyle = ScaleStyle(),
    minWeight : Int = 20,
    maxWeight : Int = 250,
    initialWeight : Int = 80,
    onWeightChange : (Int) -> Unit
) {

    val radius = scaleStyle.radius

    val scaleWidth = scaleStyle.scaleWidth

    //center of canvas
    var center by remember {
        mutableStateOf(Offset.Zero)
    }

    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    Canvas(modifier = modifier.background(color = androidx.compose.ui.graphics.Color.Red)) {
        center = this.center
        circleCenter = Offset(center.x,scaleWidth.toPx()/2f + radius.toPx())

        val outerRadius = radius.toPx() + scaleWidth.toPx()/2f
        val innerRadius = radius.toPx() - scaleWidth.toPx()/2f

        drawContext.canvas.nativeCanvas.apply {

            drawCircle(
                circleCenter.x,
                circleCenter.y,
                radius.toPx(),
                Paint().apply {
                    color = android.graphics.Color.WHITE
                    strokeWidth = scaleWidth.toPx()
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

//        Draw lines
        for (weight in minWeight..initialWeight){

            val angle = Math.toRadians((90 + initialWeight - weight).toDouble())

            val startOffset = Offset(
                x = (center.x - outerRadius * cos(angle)).toFloat(),
                y = (circleCenter.y - (outerRadius * sin(angle))).toFloat()
            )

            val endOffset = Offset(
                x = (circleCenter.x - (outerRadius.dp - scaleStyle.tenStepLineLength).toPx() * cos(angle)).toFloat(),
                y = (circleCenter.y - (outerRadius.dp - scaleStyle.tenStepLineLength).toPx() * sin(angle)).toFloat()
            )

            drawLine(
                color = androidx.compose.ui.graphics.Color.Green,
                start = startOffset,
                end = endOffset,

            )

            println("for weight $weight the start is $startOffset, end is $endOffset")


        }




    }




}







@Preview(showBackground = true)
@Composable
private fun PreviewScale() {
    Box(
        modifier = Modifier.fillMaxSize()

    ) {
        Scale(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {

        }

    }
}