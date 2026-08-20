package com.mhd_07.courtly.feature_match.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun PlayerNamesText(
    p1: String?,
    p2: String?,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    val initialText = remember(p1, p2) {
        listOfNotNull(p1?.takeIf { it.isNotBlank() }, p2?.takeIf { it.isNotBlank() })
            .joinToString(" & ")
    }

    var displayText by remember(initialText) { mutableStateOf(initialText) }

    if (displayText.isNotEmpty()) {
        Text(
            text = displayText,
            style = style,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            modifier = modifier,
            onTextLayout = { result ->
                if (result.hasVisualOverflow) {
                    val visibleCharCount = result.getLineEnd(0, visibleEnd = true)
                    val truncated = getTxtWithEllipses(p1, p2, visibleCharCount)
                    if (truncated != displayText) {
                        displayText = truncated
                    }
                }
            }
        )
    }
}

fun getTxtWithEllipses(txt1: String?, txt2: String?, maxLength: Int): String {
    val name1 = txt1?.takeIf { it.isNotBlank() }
    val name2 = txt2?.takeIf { it.isNotBlank() }

    if (name1 == null && name2 == null) return ""
    if (maxLength <= 3) return "…"

    // Single player cases
    if (name1 != null && name2 == null) {
        return if (name1.length <= maxLength) name1 else "${name1.take((maxLength - 1).coerceAtLeast(0))}…"
    }
    if (name1 == null && name2 != null) {
        return if (name2.length <= maxLength) name2 else "${name2.take((maxLength - 1).coerceAtLeast(0))}…"
    }

    // Two players case
    val fullText = "$name1 & $name2"
    if (fullText.length <= maxLength) return fullText

    // Distribute remaining characters evenly between both names minus the " & " separator (3 chars)
    val availableForNames = (maxLength - 3).coerceAtLeast(0)
    val perNameLimit = availableForNames / 2

    val truncated1 = if (name1!!.length > perNameLimit) {
        "${name1.take((perNameLimit - 1).coerceAtLeast(0))}…"
    } else name1

    val truncated2 = if (name2!!.length > perNameLimit) {
        "${name2.take((perNameLimit - 1).coerceAtLeast(0))}…"
    } else name2

    return "$truncated1 & $truncated2"
}