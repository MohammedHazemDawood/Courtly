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
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.domain.model.Team
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.enter
import com.mhd_07.courtly.core.presentation.ui.theme.exit
import com.mhd_07.courtly.feature_match_record.domain.model.TimelineAction
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.sub
import courtly.shared.generated.resources.sub_in
import courtly.shared.generated.resources.sub_out
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ArrowsUpDown
import dev.seyfarth.tablericons.outlined.User
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.until
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
fun Transfer(
    action: TimelineAction.Transfer,
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
                Text(text = stringResource(Res.string.sub))

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
                    text = stringResource(Res.string.sub_in),
                    style = MaterialTheme.typography.labelSmall,
                    color = enter
                )
                Spacer(modifier = Modifier.size(dimension.xxSmall))
                Text(text = action.player1.name)
                Text(
                    text = if (action.from == Side.TeamLeft) teamLeft.name else teamRight.name,
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
                    text = stringResource(Res.string.sub_out),
                    style = MaterialTheme.typography.labelSmall,
                    color = exit
                )
                Spacer(modifier = Modifier.size(dimension.xxSmall))
//                Text(text = if (action.from == Side.TeamLeft) teamRight.players[action.indexTo].name else teamLeft.players[action.indexTo].name)
                Text(
                    text = if (action.from == Side.TeamLeft) teamRight.name else teamLeft.name,
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

/*
@Preview
@Composable
fun TransferPreview() {
    CourtlyTheme(darkTheme = true) {
        Transfer(
            action = TimelineAction.Transfer(from = Side.TeamLeft, indexFrom = 0, indexTo = null),
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
*/
