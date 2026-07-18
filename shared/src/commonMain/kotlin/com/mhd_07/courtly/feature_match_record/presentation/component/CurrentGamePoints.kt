package com.mhd_07.courtly.feature_match_record.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mhd_07.courtly.core.domain.model.Score
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions

@Composable
fun CurrentGamePoints(
    modifier: Modifier,
    teamLeftScore: Score,
    teamRightScore: Score,
    onPoint: (Side) -> Unit
) {
    val dimension = LocalDimensions.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dimension.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ScoreCard(
            modifier = Modifier.weight(1f).aspectRatio(1f),
            score = teamLeftScore,
            onPoint = { onPoint(Side.TeamLeft) }
        )
        ScoreCard(
            modifier = Modifier.weight(1f).aspectRatio(1f),
            score = teamRightScore,
            onPoint = { onPoint(Side.TeamRight) }
        )
    }
}

