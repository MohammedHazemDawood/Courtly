package com.mhd_07.courtly.feature_match.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.feature_match.domain.model.Event
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.points
import courtly.shared.generated.resources.scored_points
import courtly.shared.generated.resources.top_scorers
import org.jetbrains.compose.resources.stringResource
import kotlin.math.max

@Composable
fun Stats(timeline: List<Event>, players: List<Player>, team1Name: String, team2Name: String) {

    val (team1Points, team2Points, sortedPlayerMap) = remember(timeline, players) {
        val source = timeline.filter { it is Event.Team1Point || it is Event.Team2Point }
        val grouped = source.groupBy { it::class }

        val players =
            players.associateWith { player -> source.count { it is Event.Team1Point && player == it.player || it is Event.Team2Point && player == it.player } }

        Triple(
            grouped[Event.Team1Point::class]?.size ?: 0,
            grouped[Event.Team2Point::class]?.size ?: 0,
            players
        ).also {
            println("players: $it")
        }
    }

    val totalPoints = team1Points + team2Points

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.small),
    ) {
        Stat(
            title = stringResource(Res.string.points),
            firsLabel = team1Name,
            secondLabel = team2Name,
            firstPercent = team1Points.toFloat() / totalPoints,
            secondPercent = team2Points.toFloat() / totalPoints,
            firsValue = team1Points,
            secondValue = team2Points
        )
        if (totalPoints > 5)
            TopScorers(
                p1 = sortedPlayerMap.keys.elementAtOrNull(0),
                p2 = sortedPlayerMap.keys.elementAtOrNull(1),
                player1Score = sortedPlayerMap.values.elementAtOrNull(0) ?: 0,
                player2Score = sortedPlayerMap.values.elementAtOrNull(1) ?: 0
            )
    }


}


@Composable
fun TopScorers(p1: Player?, p2: Player?, player1Score: Int, player2Score: Int) {
    val dimensions = LocalDimensions.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
    ) {
        if (player1Score == 0 && player2Score == 0) return
        Text(stringResource(Res.string.top_scorers), maxLines = 1)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensions.small)
        ) {
            p1?.let {
                TopScorer(
                    player = it,
                    score = player1Score,
                    rank = 1,
                    modifier = Modifier.weight(1f)
                )
            }
            p2?.let {
                TopScorer(
                    player = it,
                    score = player2Score,
                    rank = 2,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun TopScorer(player: Player, score: Int, rank: Int, modifier: Modifier = Modifier) {
    val dimensions = LocalDimensions.current
    if (score == 0) return
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(dimensions.small),
            verticalArrangement = Arrangement.spacedBy(dimensions.small),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "#$rank", style = notesTextStyle, maxLines = 1)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensions.xSmall),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PlayerAvatar(
                    avatar = player.avatar + "?v=" + player.avatarVersion,
                    name = player.name,
                    modifier = Modifier.fillMaxWidth(0.6f),
                    borderColor = MaterialTheme.colorScheme.surface
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = player.name, maxLines = 1, textAlign = TextAlign.Center)
                    Text(
                        text = stringResource(Res.string.scored_points, score.toString()),
                        style = notesTextStyle,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun Stat(
    title: String,
    firsLabel: String,
    secondLabel: String,
    firstPercent: Float,
    secondPercent: Float,
    firsValue: Int,
    secondValue: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.xSmall),
    ) {
        Text(text = title)
        Column(
            modifier = Modifier.fillMaxWidth(),
//            verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.xxSmall)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.xSmall)
            ) {
                Text(
                    text = firsValue.toString(),
//                    style = notesTextStyle,
                    maxLines = 1,
                    modifier = Modifier.wrapContentSize()
                )
                Box(
                    modifier = Modifier.weight(1f)
                        .height(LocalDimensions.current.small)
                        .background(Color.Gray, CircleShape)
                ) {
                    Box(
                        modifier = Modifier.fillMaxHeight()
                            .fillMaxWidth(max(firstPercent, secondPercent))
                            .align(if (firstPercent > secondPercent) Alignment.CenterStart else Alignment.CenterEnd)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                    )
                }
                Text(
                    text = secondValue.toString(),
//                    style = notesTextStyle,
                    maxLines = 1,
                    modifier = Modifier.wrapContentSize()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = firsLabel, maxLines = 1, style = notesTextStyle)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = secondLabel, maxLines = 1, style = notesTextStyle)
            }
        }
    }
}