package com.jetpack.ledmatrixtimecounter

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val ribbon = listOf(
    //set the 0 to 9 list matrix

    // 0
    listOf(0, 1, 1, 1, 0), // index = 0
    listOf(1, 0, 0, 0, 1),
    listOf(1, 0, 0, 1, 1),
    listOf(1, 0, 1, 0, 1),
    listOf(1, 1, 0, 0, 1),
    listOf(1, 0, 0, 0, 1),
    listOf(0, 1, 1, 1, 0),

    listOf(0, 0, 0, 0, 0), // Spacer

    // 1
    listOf(0, 0, 1, 0, 0), // index = 8
    listOf(0, 1, 1, 0, 0),
    listOf(0, 0, 1, 0, 0),
    listOf(0, 0, 1, 0, 0),
    listOf(0, 0, 1, 0, 0),
    listOf(0, 0, 1, 0, 0),
    listOf(0, 1, 1, 1, 0),

    listOf(0, 0, 0, 0, 0), // Spacer

    // 2
    listOf(0, 1, 1, 1, 0),  // index = 16
    listOf(1, 0, 0, 0, 1),
    listOf(0, 0, 0, 0, 1),
    listOf(0, 0, 0, 1, 0),
    listOf(0, 0, 1, 0, 0),
    listOf(0, 1, 0, 0, 0),
    listOf(1, 1, 1, 1, 1),

    listOf(0, 0, 0, 0, 0), // Spacer

    // 3
    listOf(0, 1, 1, 1, 0), // index = 24
    listOf(1, 0, 0, 0, 1),
    listOf(0, 0, 0, 0, 1),
    listOf(0, 0, 1, 1, 0),
    listOf(0, 0, 0, 0, 1),
    listOf(1, 0, 0, 0, 1),
    listOf(0, 1, 1, 1, 0),

    listOf(0, 0, 0, 0, 0), // Spacer

    // 4
    listOf(0, 0, 0, 1, 0),  // index = 32
    listOf(0, 0, 1, 1, 0),
    listOf(0, 1, 0, 1, 0),
    listOf(1, 0, 0, 1, 0),
    listOf(1, 1, 1, 1, 1),
    listOf(0, 0, 0, 1, 0),
    listOf(0, 0, 0, 1, 0),

    listOf(0, 0, 0, 0, 0), // Spacer

    // 5
    listOf(1, 1, 1, 1, 1), // index = 40
    listOf(1, 0, 0, 0, 0),
    listOf(1, 1, 1, 1, 0),
    listOf(0, 0, 0, 0, 1),
    listOf(0, 0, 0, 0, 1),
    listOf(1, 0, 0, 0, 1),
    listOf(0, 1, 1, 1, 0),

    listOf(0, 0, 0, 0, 0), // Spacer

    // 6
    listOf(0, 1, 1, 1, 0), // index = 48
    listOf(1, 0, 0, 0, 1),
    listOf(1, 0, 0, 0, 0),
    listOf(1, 1, 1, 1, 0),
    listOf(1, 0, 0, 0, 1),
    listOf(1, 0, 0, 0, 1),
    listOf(0, 1, 1, 1, 0),

    listOf(0, 0, 0, 0, 0), // Spacer

    // 7
    listOf(1, 1, 1, 1, 1), // index = 56
    listOf(1, 0, 0, 0, 1),
    listOf(0, 0, 0, 1, 0),
    listOf(0, 0, 1, 0, 0),
    listOf(0, 1, 0, 0, 0),
    listOf(0, 1, 0, 0, 0),
    listOf(0, 1, 0, 0, 0),

    listOf(0, 0, 0, 0, 0), // Spacer

    // 8
    listOf(0, 1, 1, 1, 0), // index = 64
    listOf(1, 0, 0, 0, 1),
    listOf(1, 0, 0, 0, 1),
    listOf(0, 1, 1, 1, 0),
    listOf(1, 0, 0, 0, 1),
    listOf(1, 0, 0, 0, 1),
    listOf(0, 1, 1, 1, 0),

    listOf(0, 0, 0, 0, 0), // Spacer

    // 9
    listOf(0, 1, 1, 1, 0), // index = 72
    listOf(1, 0, 0, 0, 1),
    listOf(1, 0, 0, 0, 1),
    listOf(0, 1, 1, 1, 1),
    listOf(0, 0, 0, 0, 1),
    listOf(1, 0, 0, 0, 1),
    listOf(0, 1, 1, 1, 0),
)

sealed class LedShape {
    object Round: LedShape()
    object Rectangle: LedShape()
}

data class LedMatrixStyle(
    val ledShape: LedShape = LedShape.Rectangle,
    val ledWidth: Dp = 15.dp,
    val ledHeight: Dp = 15.dp,
    val ledSpacing: Dp = 0.5.dp,
    val onColor: Color = Color(0xFFFF0000),
    val offColor: Color = Color(0xFFEEEEEE)
)

@Composable
fun LedMatrixDisplay(
    number: Int = 0,
    style: LedMatrixStyle = LedMatrixStyle()
) {
    val characterRow = when(number) {
        in 0..9 -> number * 8
        else -> 0
    }
    val characterRowAnimated by animateIntAsState(
        targetValue = characterRow,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing
        )
    )

    Column {
        repeat(7) { row ->
            Row {
                repeat(5) { column ->
                    Box(
                        modifier = Modifier
                            .padding(style.ledSpacing)
                            .width(style.ledWidth)
                            .height(style.ledHeight)
                            .background(
                                color = if (ribbon[characterRowAnimated + row][column] == 1)
                                    style.onColor else style.offColor,
                                shape = RoundedCornerShape(
                                    size = if (style.ledShape is LedShape.Round) style.ledWidth else 0.dp
                                )
                            )
                    )
                }
            }
        }
    }
}

















