package com.mhd_07.courtly.feature_match.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.feature_match.domain.model.Event
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.point
import courtly.shared.generated.resources.won_game
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
fun EventGameWin(
    event: Event.Team1GameWin,
    team1Name: String,
    startTime: Instant?
) {
    val dimensions = LocalDimensions.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = dimensions.small).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {

                Box(
                    modifier = Modifier.size(dimensions.xSmall).background(
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    )
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(dimensions.xSmall),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(text = stringResource(Res.string.won_game, team1Name))
                }
                Column(
//                    verticalArrangement = Arrangement.spacedBy(dimensions.xSmall),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("${event.team1Games} - ${event.team2Games}")
                    Text("${minutes(event.createdAt, startTime)}'", style = notesTextStyle)
                }
            }
        }
    }
}

@Composable
fun EventGameWin(
    event: Event.Team2GameWin,
    team2Name: String,
    startTime: Instant?
) {
    val dimensions = LocalDimensions.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = dimensions.small).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {

                Box(
                    modifier = Modifier.size(dimensions.xSmall).background(
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    )
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(dimensions.xSmall),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(text = stringResource(Res.string.won_game, team2Name))
                }
                Column(
//                    verticalArrangement = Arrangement.spacedBy(dimensions.xSmall),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("${event.team1Games} - ${event.team2Games}")
                    Text("${minutes(event.createdAt, startTime)}'", style = notesTextStyle)
                }
            }
        }
    }
}