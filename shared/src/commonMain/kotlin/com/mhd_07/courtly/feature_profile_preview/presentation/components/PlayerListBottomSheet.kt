package com.mhd_07.courtly.feature_profile_preview.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.presentation.components.AnimatedBottomSheet
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerListBottomSheet(
    isVisible: Boolean,
    title: String,
    players: List<Player>,
    onDismissRequest: () -> Unit,
    buttonText: @Composable (Player) -> String?,
    badgeText: @Composable (Player) -> String?,
    onActionClick: (Player) -> Unit,
    onClick: (Player) -> Unit
) {
    val dimensions = LocalDimensions.current

    AnimatedBottomSheet(
        isVisible = isVisible,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .padding(horizontal = dimensions.small)
                .padding(bottom = dimensions.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.small)
        ) {
            Text(text = title)
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {
                itemsIndexed(
                    items = players,
                    key = { index, player -> player.handle ?: index.toString() }
                ) { index, player ->
                    if (index != 0) {
                        HorizontalDivider()
                    }
                    PlayerCard(
                        player = player,
                        buttonText = buttonText(player),
                        badgeText = badgeText(player),
                        onButtonClick = { onActionClick(player) },
                        modifier = Modifier.fillMaxWidth().clickable {
                            onClick(player)
                        }
                    )
                }
            }
        }
    }
}