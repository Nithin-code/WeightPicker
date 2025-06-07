package com.nithin.weightpicker.presentation


import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.withRotation
import com.nithin.weightpicker.data.ScaleStyle
import kotlin.io.path.Path
import kotlin.math.atan
import kotlin.math.atan2
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

    var angle by remember {
        mutableStateOf(0.00)
    }

    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var dragStartedAngle by remember {
        mutableStateOf(0f)
    }

    var oldAngle by remember {
        mutableStateOf(angle)
    }

    Canvas(
        modifier = modifier
            .pointerInput(true){
                detectDragGestures(
                    onDragStart = { offset: Offset ->
                        dragStartedAngle = atan2(
                            y = (offset.y - circleCenter.y),
                            x = (offset.x - circleCenter.x)
                        )
                    },
                    onDragEnd = {
                        oldAngle = angle
                    }
                ) { change, _ ->
                    val touchAngle = atan2(
                        y = (change.position.y - circleCenter.y),
                        x = (change.position.x - circleCenter.x)
                    )
                    angle = oldAngle + (touchAngle - dragStartedAngle)
                }
            }
    ) {
        center = this.center
        circleCenter = Offset(center.x,scaleWidth.toPx()/2f + radius.toPx())

        val outerRadius = radius.toPx() + scaleWidth.toPx()/2f
        val innerRadius = radius.toPx() - scaleWidth.toPx()/2f

        println(
            "outer radius: $outerRadius"
        )
        println(
            "inner radius: $innerRadius"
        )

        println(
            "circle Center: $circleCenter"
        )

        drawContext.canvas.nativeCanvas.apply {

            drawCircle(
                circleCenter.x,
                circleCenter.y,
                radius.toPx(),
                Paint().apply {
                    color = Color.WHITE
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
        for (weight in minWeight..maxWeight){

            angle = Math.toRadians(180-(90 + initialWeight - weight).toDouble())

            val lineType = when(weight%10){
                0 -> LineType.TenStepLineType(length = scaleStyle.tenStepLineLength, color = scaleStyle.tenStepLineColor)
                5 -> LineType.FiveStepLineType(length = scaleStyle.fiveStepLineLength, color = scaleStyle.fiveStepLineColor)
                else -> LineType.NormalLineType(length = scaleStyle.normalLineLength, color = scaleStyle.normalLineColor)
            }

            val startOffset = Offset(
                x = (circleCenter.x - (outerRadius * cos(angle))).toFloat(),
                y = (circleCenter.y - (outerRadius * sin(angle))).toFloat()
            )

            val endOffset = Offset(
                x = (circleCenter.x - (outerRadius - lineType.lineLength.toPx()) * cos(angle)).toFloat(),
                y = (circleCenter.y - (outerRadius - lineType.lineLength.toPx()) * sin(angle)).toFloat()
            )


            drawContext.canvas.nativeCanvas.apply {

                drawLine(
                    color = lineType.lineColor,
                    start = startOffset,
                    end = endOffset,
                    strokeWidth = 3f
                )

                val x = (circleCenter.x - (outerRadius - lineType.lineLength.toPx() - 5.dp.toPx()-18.sp.toPx()) * cos(angle)).toFloat()
                val y = (circleCenter.y - (outerRadius - lineType.lineLength.toPx()-5.dp.toPx()-18.sp.toPx()) * sin(angle)).toFloat()

                if (lineType is LineType.TenStepLineType){

                    withRotation(
                        degrees = (angle * (180f/Math.PI.toFloat())-90f).toFloat(),
                        pivotX = x,
                        pivotY = y
                    ) {
                        drawText(
                            "$weight",
                            x,
                            y,
                            Paint().apply {
                                Color.BLACK
                                textSize = scaleStyle.textSize.toPx()
                                textAlign = Paint.Align.CENTER
                            }
                        )
                    }




                }

                withRotation {

                }

                when(lineType){
                    is LineType.TenStepLineType -> {

                    }

                    is LineType.FiveStepLineType -> {

                    }
                    is LineType.NormalLineType -> {

                    }
                }


                val middleTop = Offset(
                    center.x,
                    (circleCenter.y) - (innerRadius + scaleStyle.scaleIndicatorCLength.toPx())
                )

                val bottomStart = Offset(
                    center.x - 4f,
                    (circleCenter.y - innerRadius)
                )

                val bottomEnd = Offset(
                    center.x + 4f,
                    (circleCenter.y - innerRadius)
                )

                val indicator = android.graphics.Path().apply {
                    moveTo(middleTop.x, middleTop.y)
                    lineTo(bottomStart.x, bottomStart.y)
                    lineTo(bottomEnd.x,bottomEnd.y)
                    lineTo(middleTop.x, middleTop.y)
                }

                drawPath(
                    indicator,
                    Paint().apply {
                        color = scaleStyle.scaleIndicatorColor.toArgb()
                    }
                )


            }

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