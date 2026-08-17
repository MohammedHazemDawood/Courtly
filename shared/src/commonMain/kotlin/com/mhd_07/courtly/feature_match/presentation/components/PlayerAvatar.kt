package com.mhd_07.courtly.feature_match.presentation.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage

@Composable
fun PlayerAvatar(
    avatar: String?,
    name :  String,
    modifier: Modifier = Modifier,
    contentPadding: Dp = 16.dp,
    borderWidth: Dp = 4.dp,
    borderColor: Color = MaterialTheme.colorScheme.background
) {
    val initials = remember(name) { name.getInitials() }
    SubcomposeAsyncImage(
        model = avatar,
        contentDescription = name,
        error = {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (initials.isNotEmpty()) {
                    BasicText(
                        text = initials,
                        modifier = Modifier.padding(contentPadding),
//                        color = MaterialTheme.colorScheme.background,
//                        fontSize = dynamicFontSize,
//                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false,
                        autoSize = TextAutoSize.StepBased()
                    )
                }
            }
        },
        contentScale = ContentScale.Crop,
        modifier = modifier.aspectRatio(1f).clip(CircleShape).border(borderWidth, borderColor, CircleShape)
    )

}

private fun String.getInitials(): String {
    return this
        .split(" ")
        .take(2)
        .joinToString("") { it.firstOrNull()?.uppercase() ?: "" }
}