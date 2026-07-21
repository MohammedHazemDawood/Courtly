package com.mhd_07.courtly.feature_match_record.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.domain.model.Team
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.feature_match_record.domain.model.TimelineAction
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.until
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun WinMatch(
    action: TimelineAction.WinMatch,
    teamLeft: Team,
    teamRight: Team,
    startingTime: Instant
) {
    val dimension = LocalDimensions.current
    Column(
        modifier = Modifier.fillMaxWidth().padding(dimension.xSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimension.xxSmall)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimension.xSmall)
                .padding(horizontal = dimension.xSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = "${if (action.side == Side.TeamLeft) teamLeft.name else teamRight.name} won the match",
                modifier = Modifier.padding(horizontal = dimension.xSmall)
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "${startingTime.until(action.time, DateTimeUnit.MINUTE)}'",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


@Preview
@Composable
fun WinMatchPreview() {
    CourtlyTheme(darkTheme = true) {
        WinMatch(
            action = TimelineAction.WinMatch(
                side = Side.TeamLeft,
            ),
            teamLeft = Team.initial.copy(
                players = listOf(
                    Player(name = "Player 1", avatar = null, handle = null, bench = false)
                )
            ),
            teamRight = Team.initial.copy(
                players = listOf(
                    Player(name = "Player 2", avatar = null, handle = null, bench = false)
                )
            ),
            startingTime = Clock.System.now().minus(10, DateTimeUnit.MINUTE)
        )
    }
}