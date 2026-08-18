package com.mhd_07.courtly.feature_profile_preview.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.feature_match_setup.presentation.components.PlayerAvatar

@Composable
fun PlayerCard(
    player: Player,
    buttonText: String?,
    modifier: Modifier = Modifier,
    badgeText: String? = null,
    onButtonClick: () -> Unit,
) {
    val dimensions = LocalDimensions.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
    ) {
        PlayerRowContent(
            player = player,
            badgeText = badgeText,
            modifier = Modifier.weight(1f)
        )
        buttonText?.let {
            Button(
                onClick = onButtonClick,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(
                    text = it,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun PlayerRowContent(
    player: Player,
    modifier: Modifier = Modifier,
    badgeText: String? = null,
) {
    val dimensions = LocalDimensions.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
    ) {
        PlayerAvatar(avatarUrl = player.avatar)
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
            ) {
                Text(
                    text = player.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                badgeText?.let {
                    Badge {
                        Text(
                            text = it,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier.padding(horizontal = dimensions.xxSmall)
                        )
                    }
                }
            }
            player.handle?.let { handle ->
                if (handle.isNotEmpty()) {
                    Text(
                        text = "@$handle",
                        style = notesTextStyle,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}