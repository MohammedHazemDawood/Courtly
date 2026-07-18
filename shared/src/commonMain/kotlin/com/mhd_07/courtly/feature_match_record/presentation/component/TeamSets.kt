package com.mhd_07.courtly.feature_match_record.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.feature_match_record.presentation.screen.toSets

@Composable
fun TeamSets(modifier: Modifier, teamPrevWins: List<Boolean>, bestOf: Int, finished : Boolean) {
    val dimension = LocalDimensions.current
    val winsCount = teamPrevWins.count { it }
    val winner = winsCount == (bestOf / 2) + 1
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimension.xSmall)
    ) {
        val sets = teamPrevWins.toSets(bestOf)
        val dimension = LocalDimensions.current
        sets.forEachIndexed { index, win ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (winner)  MaterialTheme.colorScheme.primary else if (index == teamPrevWins.size && !finished) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (winner) MaterialTheme.colorScheme.onPrimary else if (index == teamPrevWins.size && !finished) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                ),
                modifier = Modifier.weight(1f).aspectRatio(1f),
                shape = MaterialTheme.shapes.small
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (win) "1" else "0",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(dimension.xSmall)
                    )
                }
            }
        }
    }
}
