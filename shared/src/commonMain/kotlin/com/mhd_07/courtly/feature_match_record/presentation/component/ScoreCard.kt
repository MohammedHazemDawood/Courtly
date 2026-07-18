package com.mhd_07.courtly.feature_match_record.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.mhd_07.courtly.core.domain.model.Score
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions

@Composable
fun ScoreCard(modifier: Modifier, score: Score, onPoint: () -> Unit) {
//    val dimensions = LocalDimensions.current
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = MaterialTheme.shapes.small,
        onClick = onPoint
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = score.display,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
//                modifier = Modifier.padding(dimensions.medium)
            )
        }
    }
}
