package com.mhd_07.courtly.feature_profile_preview.presentation.components
import androidx.compose.animation.animateContentSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.TextUnit
import com.mhd_07.courtly.core.presentation.ui.theme.normalTextStyle
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.read_less
import courtly.shared.generated.resources.read_more
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = TextAlign.Justify,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    collapseMaxLines: Int = 3,
    expandMaxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    showMoreText: String = stringResource(Res.string.read_more),
    showLessText: String = stringResource(Res.string.read_less)
) {
    var expanded by remember { mutableStateOf(false) }
    var expandable by remember { mutableStateOf(false) }
    var lastVisibleIndex by remember { mutableStateOf(0) }

    Text(
        text = buildAnnotatedString {
            if (expandable) {
                if (expanded) {
                    append(text.trim())
                    append(" ")
                    withLink(
                        LinkAnnotation.Clickable(
                            tag = "show_less",
                            styles = TextLinkStyles(
                                style = normalTextStyle.copy(
//                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                ).toSpanStyle()
                            )
                        ) {
                            expanded = false
                        }) { append(showLessText) }
                } else {
                    append(
                        text.substring(
                            startIndex = 0,
                            endIndex = lastVisibleIndex - (showMoreText.length + "... ".length)
                        ).trim().trim('.')
                    )
                    append("... ")
                    withLink(
                        LinkAnnotation.Clickable(
                            tag = "show_more",
                            styles = TextLinkStyles(
                                style = normalTextStyle.copy(
//                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                ).toSpanStyle()
                            )
                        ) {
                            expanded = true
                        }) { append(showMoreText) }
                }
            } else append(text.trim())
        },
        modifier = modifier.animateContentSize(),
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        textDecoration = textDecoration,
        textAlign = textAlign,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = if (expanded) expandMaxLines else collapseMaxLines,
        style = style,
        onTextLayout = {
            if (!expanded && it.hasVisualOverflow) {
                expandable = true
                lastVisibleIndex = it.getLineEnd(collapseMaxLines - 1)
            }
        }
    )
}
