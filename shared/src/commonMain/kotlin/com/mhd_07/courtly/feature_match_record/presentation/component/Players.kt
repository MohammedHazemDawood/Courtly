package com.mhd_07.courtly.feature_match_record.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mhd_07.courtly.core.domain.model.Team
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.User

@Composable
fun Players(
    teamLeft: Team,
    teamRight: Team
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
            Text(text = "Active") //TODO: Add string Res
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
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall, Alignment.Start)
                ) {
                    if (index < activeLeft.size) {
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
                            Text(text = activeLeft[index].name, overflow = TextOverflow.Ellipsis)
                            Text(
                                text = "@${activeLeft[index].handle ?: ""}",
                                color = Color.Gray,
                                style = MaterialTheme.typography.labelSmall,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(0.4f))
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall, Alignment.End)
                ) {
                    if (index < activeRight.size) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = activeRight[index].name, overflow = TextOverflow.Ellipsis)
                            Text(
                                text = "@${activeRight[index].handle ?: ""}",
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
            }
        }
        item {
            Text(text = "Bench") //TODO: Add string Res
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
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall, Alignment.Start)
                ) {
                    if (index < benchLeft.size) {
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
                            Text(text = benchLeft[index].name, overflow = TextOverflow.Ellipsis)
                            Text(
                                text = "@${benchLeft[index].handle ?: ""}",
                                color = Color.Gray,
                                style = MaterialTheme.typography.labelSmall,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(0.4f))
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall, Alignment.End)
                ) {
                    if (index < benchRight.size) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = benchRight[index].name, overflow = TextOverflow.Ellipsis)
                            Text(
                                text = "@${benchRight[index].handle ?: ""}",
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
            }
        }
    }
}

@Preview
@Composable
fun PlayersPreview() {
    CourtlyTheme(darkTheme = true) {
        Players(
            teamLeft = Team.initial.copy(
                players = listOf(
                    com.mhd_07.courtly.core.domain.model.Player(
                        name = "Alice",
                        avatar = null,
                        handle = "alice",
                        bench = false
                    ),
                    com.mhd_07.courtly.core.domain.model.Player(
                        name = "Bob",
                        avatar = null,
                        handle = "bob",
                        bench = false
                    ),
                    com.mhd_07.courtly.core.domain.model.Player(
                        name = "Eve",
                        avatar = null,
                        handle = "eve",
                        bench = true
                    )
                ),

                ),
            teamRight = Team.initial.copy(
                players = listOf(
                    com.mhd_07.courtly.core.domain.model.Player(
                        name = "Charlie",
                        avatar = null,
                        handle = "charlie",
                        bench = false
                    ),
                    com.mhd_07.courtly.core.domain.model.Player(
                        name = "Dana",
                        avatar = null,
                        handle = "dana",
                        bench = true
                    )
                )
            )
        )
    }
}