package com.mhd_07.courtly.feature_match_record.presentation.component

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.Density
import com.mhd_07.courtly.core.domain.model.HCourtSide
import com.mhd_07.courtly.core.domain.model.Side

@Composable
fun Court(
    modifier: Modifier,
    fill: Color,
    stroke: Color,
    side: Side?,
    hCourtSide: HCourtSide,
    win: Boolean
) {
    var width by remember { mutableStateOf(0f) }
    val density = LocalDensity.current
    val lineStart by animateOffsetAsState(
        targetValue = when {
            (side == Side.TeamLeft && hCourtSide == HCourtSide.Left) -> Offset(
                0f,
                0f
            )

            (side == Side.TeamLeft && hCourtSide == HCourtSide.Right) -> Offset(
                0f,
                width * 0.25f
            )

            (side == Side.TeamRight && hCourtSide == HCourtSide.Right) -> Offset(
                width,
                0f
            )

            (side == Side.TeamRight && hCourtSide == HCourtSide.Left) -> Offset(
                width,
                width * 0.25f
            )

            else -> Offset(0f, 0f)
        },
//        animationSpec =
    )
    Canvas(modifier = modifier) {
        width = size.width
//        val height = size.height
        drawRect(
            color = stroke,
            style = Stroke(width = 5f),
            topLeft = Offset(0f, 0f),
            size = Size(width = width * 0.15f, height = width * 0.5f)
        )
        drawRect(
            color = stroke,
            style = Stroke(width = 5f),
            topLeft = Offset(width - (width * 0.15f), 0f),
            size = Size(width = width * 0.15f, height = width * 0.5f)
        )

        //LeftSideMid
        drawRect(
            color = stroke,
            style = Stroke(
                width = 5f
            ),
            topLeft = Offset(width * 0.15f, 0f),
            size = Size(width = width * 0.35f, height = width * 0.25f)
        )
        drawRect(
            color = stroke,
            style = Stroke(
                width = 5f
            ),
            topLeft = Offset(width * 0.15f, width * 0.25f),
            size = Size(width = width * 0.35f, height = width * 0.25f)
        )

        //RightSideMid
        drawRect(
            color = stroke,
            style = Stroke(width = 5f),
            topLeft = Offset(width * 0.5f, 0f),
            size = Size(width = width * 0.35f, height = width * 0.25f)
        )
        drawRect(
            color = stroke,
            style = Stroke(
                width = 5f
            ),
            topLeft = Offset(width * 0.5f, width * 0.25f),
            size = Size(width = width * 0.35f, height = width * 0.25f)
        )

        if (side != null && !win)
            drawLine(
                color = fill,
                strokeWidth = 5f,
//                style = Fill,
                start = lineStart,
                end = lineStart + Offset(0f, width * 0.25f),
//                size = Size(width = width * 0.15f, height = width * 0.25f)
            )
        /* drawText(
             textLayoutResult = TextMeasurer(
                 defaultDensity = density,
                 defaultFontFamilyResolver = LocalFontFamilyResolver.current,
                 defaultLayoutDirection = TODO(),
                 cacheSize = TODO(),
             ).measure("Player Name")
         ) *///TODO: Add Player Name
    }
}
