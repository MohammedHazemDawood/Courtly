package com.mhd_07.courtly.feature_sign.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.normalTextStyle
import com.mhd_07.courtly.core.util.AspectRatioReference
import com.mhd_07.courtly.core.util.aspectRatioReference
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.check_read_outline
import org.jetbrains.compose.resources.painterResource


@Composable
fun NumericPagerIndicator(modifier: Modifier = Modifier, stepsCount: Int, currentStep: Int) {
    val dimensions = LocalDimensions.current
    Row(
        modifier = modifier.heightIn(max = dimensions.large),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            dimensions.xSmall,
            Alignment.CenterHorizontally
        )
    ) {
        repeat(stepsCount) {
            Box(
                modifier = Modifier.aspectRatioReference(1f, 1f, AspectRatioReference.MIN_PARENT_WIDTH_PARENT_HEIGHT).background(
                    if (it <= currentStep) MaterialTheme.colorScheme.primary else Color.Gray,
                    CircleShape
                ),
                contentAlignment = Alignment.Center,
            ) {
                if (it < currentStep)
                    Icon(
                        painter = painterResource(Res.drawable.check_read_outline),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                else
                    Text(
                        text = (it + 1).toString(),
                        style = normalTextStyle,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(vertical = dimensions.xxSmall)
                    )
            }
            if (it != stepsCount - 1)
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray)
        }
    }
}

@Composable
fun PagerIndicator(modifier: Modifier = Modifier, stepsCount: Int, currentStep: Int) {
    val dimensions = LocalDimensions.current
    Row(
        modifier = modifier/*.heightIn(max = dimensions.large)*/,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            dimensions.xxSmall,
            Alignment.CenterHorizontally
        )
    ) {
        repeat(stepsCount) {
            if (it != 0)
                HorizontalDivider(modifier = Modifier.weight(1f), color = if (it <= currentStep) MaterialTheme.colorScheme.primary else Color.Gray)
            Box(
                modifier = Modifier.size(dimensions.small).aspectRatioReference(1f, 1f, AspectRatioReference.MIN_PARENT_WIDTH_PARENT_HEIGHT).background(
                    if (it <= currentStep) MaterialTheme.colorScheme.primary else Color.Gray,
                    CircleShape
                ),
            )
        }
    }
}
