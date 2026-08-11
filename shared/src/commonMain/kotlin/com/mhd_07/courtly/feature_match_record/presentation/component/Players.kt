package com.mhd_07.courtly.feature_match_record.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.model.Team
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.bench
import courtly.shared.generated.resources.playing
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ArrowsUpDown
import dev.seyfarth.tablericons.outlined.User
import org.jetbrains.compose.resources.stringResource

@Composable
fun Players(
    teamLeft: Team,
    teamRight: Team,
) {
    val dimensions = LocalDimensions.current

    val activeLeft = teamLeft.players.filter { !it.bench }
    val activeRight = teamRight.players.filter { !it.bench }

    val benchLeft = teamLeft.players.filter { it.bench }
    val benchRight = teamRight.players.filter { it.bench }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(dimensions.xSmall),
        verticalArrangement = Arrangement.spacedBy(dimensions.small)
    ) {
        item {
            Text(text = stringResource(Res.string.playing))
        }
        items(
            count = maxOf(
                activeLeft.size,
                activeRight.size
            )
        ) { index ->
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (index < activeLeft.size)
                    PlayerLeft(
                        modifier = Modifier.weight(1f),
                        name = activeLeft[index].name,
                        handle = activeLeft[index].handle,
                        avatar = activeLeft[index].avatar,
                        alignment = Alignment.Start
                    )
                else Box(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.weight(0.4f))
                if (index < activeRight.size)
                    PlayerRight(
                        modifier = Modifier.weight(1f),
                        name = activeRight[index].name,
                        handle = activeRight[index].handle,
                        avatar = activeRight[index].avatar,
                        alignment = Alignment.End
                    )
                else Box(modifier = Modifier.weight(1f))
            }
        }
        item {
            Text(text = stringResource(Res.string.bench))
        }
        items(
            count = maxOf(
                benchLeft.size,
                benchRight.size
            )
        ) { index ->
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (index < benchLeft.size)
                    PlayerLeft(
                        modifier = Modifier.weight(1f),
                        name = benchLeft[index].name,
                        handle = benchLeft[index].handle,
                        avatar = benchLeft[index].avatar,
                        alignment = Alignment.Start
                    )
                else Box(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.weight(0.4f))
                if (index < benchRight.size)
                    PlayerRight(
                        modifier = Modifier.weight(1f),
                        name = benchRight[index].name,
                        handle = benchRight[index].handle,
                        avatar = benchRight[index].avatar,
                        alignment = Alignment.End
                    )
                else Box(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun PlayerLeft(
    modifier: Modifier,
    name: String,
    handle: String?,
    avatar: Any?,
    alignment: Alignment.Horizontal
) {
    val dimensions = LocalDimensions.current
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
//        if (index < activeLeft.size) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall, alignment)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = MaterialTheme.shapes.small,
//                modifier = Modifier.fillMaxWidth(0.2f).aspectRatio(1f)
            ) {
                //TODO: Use Coil
                Icon(
                    imageVector = TablerIcons.Outlined.User,
                    contentDescription = null,
                    modifier = Modifier.padding(dimensions.xSmall)
                )
            }
            Column(/*verticalArrangement = Arrangement.spacedBy(dimensions.xxSmall)*/) {
                Text(text = name, overflow = TextOverflow.Ellipsis)
                Text(
                    text = "@${handle ?: ""}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelSmall,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        IconButton(onClick = {}) {
            Icon(
                imageVector = TablerIcons.Outlined.ArrowsUpDown,
                contentDescription = "Substitute",
                modifier = Modifier.size(dimensions.small)
            )//Use String res
        }
    }
//    }
}

@Composable
fun PlayerRight(
    modifier: Modifier,
    name: String,
    handle: String?,
    avatar: Any?,
    alignment: Alignment.Horizontal
) {
    val dimensions = LocalDimensions.current
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
//        if (index < activeLeft.size) {
        IconButton(onClick = {}) {
            Icon(
                imageVector = TablerIcons.Outlined.ArrowsUpDown,
                contentDescription = "Substitute",
                modifier = Modifier.size(dimensions.small)
            )//Use String res
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall, alignment)
        ) {
            Column(/*verticalArrangement = Arrangement.spacedBy(dimensions.xxSmall)*/
                horizontalAlignment = alignment
            ) {
                Text(text = name, overflow = TextOverflow.Ellipsis)
                Text(
                    text = "@${handle ?: ""}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelSmall,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = MaterialTheme.shapes.small,
//                modifier = Modifier.fillMaxWidth(0.2f).aspectRatio(1f)
            ) {
                //TODO: Use Coil
                Icon(
                    imageVector = TablerIcons.Outlined.User,
                    contentDescription = null,
                    modifier = Modifier.padding(dimensions.xSmall)
                )
            }
        }
    }
//    }
}

@Preview
@Composable
fun PlayersPreview() {
    CourtlyTheme(darkTheme = true) {
        Players(
            teamLeft = Team.initial.copy(
                players = listOf(
                    Player(
                        name = "Alice",
                        avatar = null,
                        handle = "alice",
                        bench = false,
                        bio = "",
                        avatarVersion = 0
                    ),
                    Player(
                        name = "Bob",
                        avatar = null,
                        handle = "bob",
                        bench = false,
                        bio = "",
                        avatarVersion = 0
                    ),
                    Player(
                        name = "Eve",
                        avatar = null,
                        handle = "eve",
                        bench = true,
                        bio = "",
                        avatarVersion = 0
                    )
                ),

                ),
            teamRight = Team.initial.copy(
                players = listOf(
                    Player(
                        name = "Charlie",
                        avatar = null,
                        handle = "charlie",
                        bench = false,
                        bio = "",
                        avatarVersion = 0
                    ),
                    Player(
                        name = "Dana",
                        avatar = null,
                        handle = "dana",
                        bench = true,
                        bio = "",
                        avatarVersion = 0
                    )
                )
            ),
        )
    }
}