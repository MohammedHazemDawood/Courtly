package com.mhd_07.courtly.feature_match.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun PlayerNamesText(
    p1: String?,
    p2: String?,
    style: TextStyle
) {
    var displayText by remember(
        p1,
        p2
    ) {
        mutableStateOf(
            "${
                if (!p1.isNullOrEmpty()) p1 else "" + if (p1?.isNotEmpty()?.not() == true && !p2.isNullOrEmpty()
                ) " & " else ""
            }${if (!p2.isNullOrEmpty()) p2 else ""}"
        )
    }

    Text(
        text = displayText,
        style = style,
        maxLines = 1,
        overflow = TextOverflow.Clip,
        modifier = if (displayText == "") Modifier.height(0.dp) else Modifier,
        onTextLayout = { result ->
            if (result.hasVisualOverflow) {
                val endCharIndex = result.getLineEnd(0, visibleEnd = true)
                val excess = ((p1?.length ?: 0) + (p2?.length ?: 0) + 3) - endCharIndex
                if (excess > 0) {
                    val trimCount = (excess / 2 + 2).coerceAtLeast(1)
                    val newP1 = p1?.dropLast(trimCount.coerceAtMost(p1.length))
                    val newP2 = p2?.dropLast(trimCount.coerceAtMost(p2.length))
                    displayText =
                        if (newP1?.length == 0 || newP2?.length == 0) "" else "$newP1.. & $newP2.."
                }
            }
        }
    )
}