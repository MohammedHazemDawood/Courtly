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
                if (!p1.isNullOrEmpty()) p1 else "" + if (p1?.isNotEmpty()
                        ?.not() == true && !p2.isNullOrEmpty()
                ) " & " else ""
            }${if (!p2.isNullOrEmpty()) p2 else ""}"
        )
    }

    if (displayText.isNotEmpty())
        Text(
            text = displayText,
            style = style,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            modifier = if (displayText == "") Modifier.height(0.dp) else Modifier,
            onTextLayout = { result ->
                if (result.hasVisualOverflow) {
                    val endCharIndex = result.getLineEnd(0, visibleEnd = true)
                    displayText = getTxtWithEllipses(p1, p2, endCharIndex)
                }
            }
        )
}

fun getTxtWithEllipses(txt1: String?, txt2: String?, maxLength: Int): String {
    if (txt1.isNullOrEmpty() && txt2.isNullOrEmpty()) return ""
    if (maxLength < 8) return ""
    if (txt1 == null) return "${txt2?.take(maxLength - 2)}.."
    if (txt2 == null) return "${txt1.take(maxLength - 2)}.."
    if (txt1.length + txt2.length <= maxLength + 3) return "$txt1 & $txt2"
    val excess = (txt1.length + txt1.length + 3) - maxLength
    if (excess < 8) return ""
    return "${txt1.take(excess / 2)} & ${txt2.take(excess / 2)}"

}