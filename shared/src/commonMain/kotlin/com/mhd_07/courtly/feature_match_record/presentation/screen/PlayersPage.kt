package com.mhd_07.courtly.feature_match_record.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import coil3.compose.SubcomposeAsyncImage
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.titleTextStyle
import com.mhd_07.courtly.core.util.BackHandler
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.add_player
import courtly.shared.generated.resources.add_square_outline
import courtly.shared.generated.resources.remove_player
import courtly.shared.generated.resources.user_outline
import courtly.shared.generated.resources.x
import org.jetbrains.compose.resources.painterResource


import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayersPage(
    title: String,
    description: String,
    players: List<Player>,
    searchText: String,
    onSearchPlayer: (String) -> Unit,
    searchResults: Map<Player, Boolean>,
    onAddPlayer: (Player) -> Unit,
    onRemovePlayer: (Player) -> Unit,
    modifier: Modifier = Modifier,
    isVisible : Boolean
) {
    val dimensions = LocalDimensions.current
    var searching by remember { mutableStateOf(false) }

    DisposableEffect(isVisible) {
        onDispose {
            onSearchPlayer("")
            searching = false
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(dimensions.small)
    ) {
        // Header
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = title, style = titleTextStyle)
            Text(text = description, style = notesTextStyle)
        }

        // Selected Players List
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
        ) {
            players.forEach { player ->
                SelectedPlayerCard(
                    player = player,
                    onRemove = { onRemovePlayer(player) }
                )
            }
        }

        // Search Section
        AnimatedVisibility(visible = searching) {
            ExposedDropdownMenuBox(
                expanded = searching,
                onExpandedChange = { /*searching = it*/ },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = onSearchPlayer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
                    placeholder = { Text("Search a player") }, // TODO: Add string resource
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                onSearchPlayer("")
                                searching = false
                            }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.x),
                                contentDescription = "Close search" // TODO: Add string resource
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    singleLine = true
                )

                ExposedDropdownMenu(
                    expanded = searching,
                    onDismissRequest = { }
                ) {
                    // Option to add as a custom player (if text is typed)
                    if (searchText.isNotBlank()) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.user_outline),
                                        contentDescription = null,
                                        modifier = Modifier.size(dimensions.xxLarge).clip(CircleShape)
                                    )
                                    Text("Add \"$searchText\"")//TODO: Use string resource
                                }
                            },
                            onClick = {
                                onAddPlayer(
                                    Player(
                                        name = searchText.trim('@', ' '),
                                        handle = null,
                                        bio = "",
                                        avatar = null,
                                        avatarVersion = 0
                                    )
                                )
                                onSearchPlayer("")
                                searching = false
                            }
                        )
                    }

                    // Existing search results
                    searchResults.forEach { (player, available) ->
                        DropdownMenuItem(
                            text = {
                                PlayerRowContent(player = player)
                            },
                            onClick = {
                                onAddPlayer(player)
                                onSearchPlayer("")
                                searching = false
                            },
                            enabled = available
                        )
                    }

                    // Empty state when search produces no matching registered players and input is empty
                    if (searchResults.isEmpty() && searchText.isBlank()) {
                        DropdownMenuItem(
                            text = { Text("Type a name or @handle to search") },
                            onClick = { },
                            enabled = false
                        )
                    }
                }
            }
        }

        // Add Player Button (When not searching)
        AnimatedVisibility(
            visible = !searching,
            enter = slideInHorizontally(),
            exit = slideOutHorizontally()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = dimensions.xxSmall,
                        color = Color.Gray,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable { searching = true }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensions.small),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.add_square_outline),
                        contentDescription = null
                    )
                    Text(text = stringResource(Res.string.add_player))
                }
            }
        }
    }
}

@Composable
private fun SelectedPlayerCard(
    player: Player,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dimensions = LocalDimensions.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = dimensions.xxSmall,
                color = Color.Gray,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensions.xSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PlayerRowContent(player = player)
            IconButton(onClick = onRemove) {
                Icon(
                    painter = painterResource(Res.drawable.x),
                    contentDescription = stringResource(Res.string.remove_player)
                )
            }
        }
    }
}

@Composable
private fun PlayerRowContent(
    player: Player,
    modifier: Modifier = Modifier
) {
    val dimensions = LocalDimensions.current

    Row(
        modifier = modifier/*.height(IntrinsicSize.Min)*/,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
    ) {
        PlayerAvatar(avatarUrl = player.avatar)
        Column {
            Text(text = player.name)
            player.handle?.let {
                Text(text = "@$it", style = notesTextStyle)
            }
        }
    }
}

@Composable
private fun PlayerAvatar(
    avatarUrl: String?,
    modifier: Modifier = Modifier
) {
    val dimensions = LocalDimensions.current

    SubcomposeAsyncImage(
        model = avatarUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(dimensions.xxLarge)
            .clip(CircleShape),
        error = {
            Icon(
                painter = painterResource(Res.drawable.user_outline),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    )
}