package com.mhd_07.courtly.feature_match_record.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.domain.model.Team
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.enter
import com.mhd_07.courtly.core.presentation.ui.theme.exit
import com.mhd_07.courtly.feature_match_record.domain.model.TimelineAction
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ArrowsUpDown
import dev.seyfarth.tablericons.outlined.User
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.until
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun Substitution(
    action: TimelineAction.Sub,
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimension.xxSmall)
            ) {
                Icon(
                    imageVector = TablerIcons.Outlined.ArrowsUpDown,
                    contentDescription = null,
                    modifier = Modifier.size(dimension.small),
                )
                Text(text = "Substitution") //TODO: Add string Res

            }
            Text(text = "${startingTime.until(action.time, DateTimeUnit.MINUTE)}'")

        }
        Spacer(modifier = Modifier.size(dimension.xxSmall))
        Box(modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(
                modifier = Modifier.align(Alignment.Center).fillMaxWidth(0.5f)
            )
        }
        Spacer(modifier = Modifier.size(dimension.xSmall))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = dimension.xSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "In",
                    style = MaterialTheme.typography.labelSmall,
                    color = enter
                ) //Todo: Add string Res
                Spacer(modifier = Modifier.size(dimension.xxSmall))
                Text(text = action.player1.name)
                Text(
                    text = if (action.side == Side.TeamLeft) teamLeft.name else teamRight.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = MaterialTheme.shapes.small,
//                modifier = Modifier.fillMaxWidth(0.2f).aspectRatio(1f)
            ) {
                //TODO: Use Coil
                Icon(
                    imageVector = TablerIcons.Outlined.User,
                    contentDescription = null,
                    modifier = Modifier.padding(dimension.xSmall)
                )
            }
        }
        Spacer(modifier = Modifier.size(dimension.xSmall))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = dimension.xSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "Out",
                    style = MaterialTheme.typography.labelSmall,
                    color = exit
                ) //Todo: Add string Res
                Spacer(modifier = Modifier.size(dimension.xxSmall))
                Text(text = action.player2.name)
                Text(
                    text = if (action.side == Side.TeamLeft) teamLeft.name else teamRight.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = MaterialTheme.shapes.small,
//                modifier = Modifier.fillMaxWidth(0.2f).aspectRatio(1f)
            ) {
                //TODO: Use Coil
                Icon(
                    imageVector = TablerIcons.Outlined.User,
                    contentDescription = null,
                    modifier = Modifier.padding(dimension.xSmall)
                )
            }
        }
        Spacer(modifier = Modifier.size(dimension.xSmall))
    }
}

@Preview
@Composable
fun SubPreview() {
    CourtlyTheme(darkTheme = true) {
            Substitution(
                action = TimelineAction.Sub(side = Side.TeamLeft, player1 = Player(name = "Player 1", avatar = null, handle = null, bench = false), player2 = Player(name = "Player 2", avatar = null, handle = null, bench = false)),
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
