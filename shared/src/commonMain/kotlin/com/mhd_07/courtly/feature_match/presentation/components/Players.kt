package com.mhd_07.courtly.feature_match.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.bench
import courtly.shared.generated.resources.playing
import courtly.shared.generated.resources.sub
import courtly.shared.generated.resources.transfer_horizontal_outline
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun Players(
    team1Players: List<Player>,
    team2Players: List<Player>,
    onSubOrTransfer: (Player) -> Unit,
    mine: Boolean
) {
    val dimensions = LocalDimensions.current

    val team1Active = remember(team1Players) { team1Players.filter { !it.bench } }
    val team1Bench = remember(team1Players) { team1Players.filter { it.bench } }

    val team2Active = remember(team2Players) { team2Players.filter { !it.bench } }
    val team2Bench = remember(team2Players) { team2Players.filter { it.bench } }

    var selectedPlayer by remember { mutableStateOf<Player?>(null) }

    val handlePlayerClick: (Player) -> Unit = { player ->
        when (selectedPlayer) {
            null -> selectedPlayer = player
            player -> selectedPlayer = null
            else -> {
                selectedPlayer?.let { onSubOrTransfer(it) }
                onSubOrTransfer(player)
                selectedPlayer = null
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(dimensions.small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.playing),
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensions.small)
        ) {
            // Team 1 Active
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {
                team1Active.forEach { player ->
                    PlayerCard(
                        player = player,
                        isSelected = selectedPlayer == player,
                        subEnabled = mine && selectedPlayer == null,
                        onSelect = handlePlayerClick
                    )
                }
            }

            // Team 2 Active
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {
                team2Active.forEach { player ->
                    PlayerCard(
                        player = player,
                        isSelected = selectedPlayer == player,
                        subEnabled = mine && selectedPlayer == null,
                        onSelect = handlePlayerClick
                    )
                }
            }
        }

        if (team1Bench.isNotEmpty() && team2Bench.isNotEmpty())
            Text(
                text = stringResource(Res.string.bench),
                style = MaterialTheme.typography.titleMedium
            )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensions.small)
        ) {
            // Team 1 Bench
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {
                team1Bench.forEach { player ->
                    PlayerCard(
                        player = player,
                        isSelected = selectedPlayer == player,
                        subEnabled = mine && selectedPlayer == null,
                        onSelect = handlePlayerClick
                    )
                }
            }

            // Team 2 Bench
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {
                team2Bench.forEach { player ->
                    PlayerCard(
                        player = player,
                        isSelected = selectedPlayer == player,
                        subEnabled = mine && selectedPlayer == null,
                        onSelect = handlePlayerClick
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerCard(
    player: Player,
    isSelected: Boolean,
    onSelect: (Player) -> Unit,
    modifier: Modifier = Modifier,
    subEnabled: Boolean = false
) {
    val dimensions = LocalDimensions.current
    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surface
    }

//    Card(
//        modifier = modifier
//            .fillMaxWidth()
//            .then(
//                if (subEnabled) {
//                    Modifier.clickable { onSelect(player) }
//                } else Modifier
//            ),
//        colors = CardDefaults.cardColors(containerColor = containerColor),
//        shape = MaterialTheme.shapes.medium
//    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
//                .padding(dimensions.small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayerAvatar(
                name = player.name,
                avatar = player.avatar + "?v=" + player.avatarVersion,
                modifier = Modifier.size(dimensions.xLarge),
                borderColor = MaterialTheme.colorScheme.surface
            )
            Column(modifier = Modifier.weight(1f, fill = false)) {
                Text(
                    text = player.name,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium
                )
                player.handle?.let { handle ->
                    Text(
                        text = "@$handle",
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

//            if (subEnabled) {
//                IconButton(onClick = { onSelect(player) }) {
//                    Icon(
//                        painter = painterResource(Res.drawable.transfer_horizontal_outline),
//                        contentDescription = stringResource(Res.string.sub),
//                        tint = if (isSelected) {
//                            MaterialTheme.colorScheme.primary
//                        } else {
//                            MaterialTheme.colorScheme.onSurface
//                        }
//                    )
//                }
//            }
//        }
    }
}