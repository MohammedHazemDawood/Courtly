package com.mhd_07.courtly.feature_match_record.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mhd_07.courtly.core.domain.model.HCourtSide
import com.mhd_07.courtly.core.domain.model.Score
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.domain.model.Team
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.feature_match_record.domain.model.TimelineAction
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.set
import courtly.shared.generated.resources.won_game
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.until
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun WinGame(
    action: TimelineAction.WinGame,
    teamLeft: Team,
    teamRight: Team,
    startingTime: Instant
) {
    val dimension = LocalDimensions.current
    Card(
        modifier = Modifier.fillMaxWidth().padding(dimension.small),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimension.xSmall)
                .padding(horizontal = dimension.xSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(Res.string.won_game, if (action.side == Side.TeamLeft) teamLeft.name else teamRight.name))
            Text(text = "${startingTime.until(action.time, DateTimeUnit.MINUTE)}'")
        }
        Spacer(modifier = Modifier.size(dimension.xxSmall))
        Box(modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(modifier = Modifier.align(Alignment.Center).fillMaxWidth(0.5f))
        }
        Spacer(modifier = Modifier.size(dimension.xSmall))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = dimension.xSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = teamLeft.name)
            Text(text = action.teamLeftScore.display)
            Text(text = "-")
            Text(text = action.teamRightScore.display)
            Text(text = teamRight.name)
        }
        Spacer(modifier = Modifier.size(dimension.xSmall))
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(Res.string.set), modifier = Modifier.align(Alignment.Center))
        }
        Spacer(modifier = Modifier.size(dimension.xSmall))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = dimension.small),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = action.teamLeftWins.toString())
            Text(text = action.teamRightWins.toString())
        }
        Spacer(modifier = Modifier.size(dimension.xSmall))

    }
} // TODO: Fix sets number

@Preview
@Composable
fun WinGamePreview() {
    CourtlyTheme(darkTheme = true) {
        WinGame(
            action = TimelineAction.WinGame(
                side = Side.TeamLeft,
                teamLeftScore = Score.Fifteen,
                teamRightScore = Score.Fifteen,
                ballPlayer = null,
                ballHalf = HCourtSide.Left,
                teamLeftWins = 1,
                teamRightWins = 0
            ),
            teamLeft = Team.initial.copy(
                players = listOf(
//                    Player(name = "Player 1", avatar = null, handle = null, bench = false)
                )
            ),
            teamRight = Team.initial.copy(
                players = listOf(
//                    Player(name = "Player 2", avatar = null, handle = null, bench = false)
                )
            ),
            startingTime = Clock.System.now().minus(10, DateTimeUnit.MINUTE)
        )
    }
}