package com.mhd_07.courtly.feature_profile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.domain.model.MatchType
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.model.Score
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.presentation.model.RemoteError
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.feature_match.domain.model.Match
import com.mhd_07.courtly.feature_match.presentation.components.PlayerAvatar
import com.mhd_07.courtly.feature_match.presentation.components.PlayerNamesText
import com.mhd_07.courtly.feature_match.presentation.components.TeamSetsRow
import com.mhd_07.courtly.feature_profile.presentation.components.ExpandableText
import com.mhd_07.courtly.feature_profile.presentation.components.PlayerListBottomSheet
import com.mhd_07.courtly.feature_profile.presentation.components.ProfileHeader
import com.valentinilk.shimmer.shimmer
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.cannot_find_user
import courtly.shared.generated.resources.follow
import courtly.shared.generated.resources.followers
import courtly.shared.generated.resources.follow_you
import courtly.shared.generated.resources.followings
import courtly.shared.generated.resources.live
import courtly.shared.generated.resources.map_point_outline
import courtly.shared.generated.resources.menu_dots_outline
import courtly.shared.generated.resources.settings
import courtly.shared.generated.resources.settings_outline
import courtly.shared.generated.resources.unfollow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navBack: () -> Unit,
    profile: Player?,
    followers: List<Player>,
    following: List<Player>,
    myFollowers: List<Player>,
    myFollowing: List<Player>,
    result: RemoteResult?,
    onRefresh: () -> Unit = {},
    navToSettings: () -> Unit,
    follow: (Player) -> Unit = {},
    unfollow: (Player) -> Unit = {},
    isMine: Boolean = false,
    previewProfile: (Player) -> Unit,
    myId: String?,
    matches: List<Match>,
    navToMatch : (String) -> Unit
) {
    val dimensions = LocalDimensions.current
    var followerSheetVisible by remember { mutableStateOf(false) }
    var followingSheetVisible by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {

        // Followers Bottom Sheet
        PlayerListBottomSheet(
            isVisible = followerSheetVisible,
            title = stringResource(Res.string.followers),
            players = followers,
            onDismissRequest = { followerSheetVisible = false },
            buttonText = { player ->
                if (player.id == myId) null
                else if (following.contains(player)) stringResource(Res.string.unfollow)
                else stringResource(Res.string.follow)
            },
            badgeText = { player ->
                if (myFollowers.contains(player)) stringResource(Res.string.follow_you) else null
            },
            onActionClick = { player ->
                if (myFollowing.contains(player)) unfollow(player) else follow(player)
            },
            onClick = { player -> previewProfile(player) }
        )

        // Following Bottom Sheet
        PlayerListBottomSheet(
            isVisible = followingSheetVisible,
            title = stringResource(Res.string.followings),
            players = following,
            onDismissRequest = { followingSheetVisible = false },
            buttonText = { player ->
                if (player.id == myId) null
                else if (following.contains(player)) stringResource(Res.string.unfollow)
                else stringResource(Res.string.follow)
            },
            badgeText = { player ->
                if (myFollowers.contains(player)) stringResource(Res.string.follow_you) else null
            },
            onActionClick = { player ->
                if (myFollowing.contains(player)) unfollow(player) else follow(player)
            },
            onClick = { player -> previewProfile(player) }
        )

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize(),
            isRefreshing = (result is RemoteResult.Loading),
            onRefresh = onRefresh
        ) {
            var openMenu by remember { mutableStateOf(false) }

            if (result is RemoteResult.Error && result.error == RemoteError.NotFound && profile == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(Res.string.cannot_find_user))
                }
            } else {
                val scrollState = rememberLazyListState()
                val neededScroll = with(LocalDensity.current) { (200.dp).toPx() }

                val progress by remember {
                    derivedStateOf {
                        if (scrollState.firstVisibleItemIndex > 0) {
                            1f
                        } else {
                            (scrollState.firstVisibleItemScrollOffset / neededScroll).coerceIn(
                                0f,
                                1f
                            )
                        }
                    }
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    ProfileHeader(
                        progress = progress,
                        name = profile?.name,
                        handle = profile?.handle,
                        avatar = profile?.avatar + "?v=" + profile?.avatarVersion,
                        cover = profile?.cover + "?v=" + profile?.coverVersion,
                        onBack = navBack,
                        isFollowing = myFollowers.contains(profile),
                        action = {
                            if (isMine) {
                                Box(contentAlignment = Alignment.Center) {
                                    IconButton(
                                        onClick = { openMenu = true },
                                        modifier = Modifier.border(
                                            dimensions.xxSmall,
                                            Color.Gray,
                                            CircleShape
                                        )
                                    ) {
                                        Icon(
                                            painter = painterResource(Res.drawable.menu_dots_outline),
                                            contentDescription = stringResource(Res.string.settings)
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = openMenu,
                                        onDismissRequest = { openMenu = false },
                                        shape = MaterialTheme.shapes.medium
                                    ) {
                                        DropdownMenuItem(
                                            leadingIcon = {
                                                Icon(
                                                    painter = painterResource(Res.drawable.settings_outline),
                                                    contentDescription = null
                                                )
                                            },
                                            text = { Text(text = stringResource(Res.string.settings)) },
                                            onClick = {
                                                openMenu = false
                                                navToSettings()
                                            }
                                        )
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier.shimmerable(
                                        profile == null,
                                        shape = MaterialTheme.shapes.large
                                    )
                                ) {
                                    Button(
                                        onClick = {
                                            if (profile != null) {
                                                if (myFollowing.contains(profile)) unfollow(profile)
                                                else follow(profile)
                                            }
                                        },
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .visible(profile != null),
                                    ) {
//                                        if (profile != null) {
                                        Text(
                                            text = if (myFollowing.contains(profile)) {
                                                stringResource(Res.string.unfollow)
                                            } else {
                                                stringResource(Res.string.follow)
                                            }
                                        )
//                                        }
                                    }
                                }
                            }
                        }
                    )
                    LazyColumn(
                        modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = dimensions.small),
                        state = scrollState
                    ) {
                        item(key = "details") {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = dimensions.small),
                                verticalArrangement = Arrangement.spacedBy(dimensions.small)
                            ) {
                                ExpandableText(
                                    text = profile?.bio ?: "",
                                    modifier = Modifier.fillMaxWidth()
                                        .shimmerable(enabled = profile == null)
                                )
                                if (profile?.location?.isNotEmpty() == true) {
                                    FlowRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(dimensions.small),
                                        verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall),
                                        ) {
                                            Icon(
                                                painter = painterResource(Res.drawable.map_point_outline),
                                                contentDescription = null,
                                                tint = Color.Gray
                                            )
                                            Text(
                                                text = profile.location,
                                                style = notesTextStyle
                                            )
                                        }
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(dimensions.small),
                                    modifier = Modifier.shimmerable(profile == null)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall),
                                        modifier = Modifier.visible(profile != null).clickable {
                                            followerSheetVisible = true
                                        }
                                    ) {
                                        Text(text = followers.size.toString())
                                        Text(
                                            text = stringResource(Res.string.followers),
                                            style = notesTextStyle
                                        )
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall),
                                        modifier = Modifier.visible(profile != null).clickable {
                                            followingSheetVisible = true
                                        }
                                    ) {
                                        Text(text = following.size.toString())
                                        Text(
                                            text = stringResource(Res.string.followings),
                                            style = notesTextStyle
                                        )
                                    }
                                }
                            }
                        }

                        // Matches / Profile Content
                        items(matches, key = { it.id }) { match ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = dimensions.small),
                                shape = MaterialTheme.shapes.medium,
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                onClick = {
                                    navToMatch(match.id)
                                }) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(dimensions.small),
                                    verticalArrangement = Arrangement.spacedBy(dimensions.small)
                                ) {
                                    if (match.status == MatchStatus.Live)
                                        SuggestionChip(
                                            onClick = { },
                                            label = { Text(text = stringResource(Res.string.live)) },
                                            icon = {
                                                Box(
                                                    modifier = Modifier.fillMaxHeight()
                                                        .background(MaterialTheme.colorScheme.onSurface, CircleShape)
                                                )
                                            },
                                            colors = SuggestionChipDefaults.suggestionChipColors(
                                                containerColor = MaterialTheme.colorScheme.error
                                            ),
                                            modifier = Modifier.align(Alignment.End)
                                        )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(
                                                dimensions.xSmall
                                            ),
                                            modifier = Modifier.weight(1f, fill = false)
                                        ) {
                                            val team1ActivePlayers =
                                                match.team1.players.filter { !it.bench }

                                            Box(
                                                modifier = Modifier/*height(dimensions.xxLarge).*/.height(
                                                    dimensions.xxLarge
                                                )
                                                    .aspectRatio(
                                                        if (match.rules.type == MatchType.Single || team1ActivePlayers.getOrNull(
                                                                1
                                                            ) == null
                                                        ) 1f else 1.5f
                                                    )
                                            ) {
                                                team1ActivePlayers.getOrNull(0)?.let {
                                                    PlayerAvatar(
                                                        name = it.name,
                                                        avatar = it.avatar + "?v=" + it.avatarVersion,
                                                        modifier = Modifier.align(Alignment.CenterStart),
                                                        contentPadding = dimensions.small,
                                                        borderColor = MaterialTheme.colorScheme.surface

                                                    )
                                                }
                                                team1ActivePlayers.getOrNull(1)?.let {
                                                    PlayerAvatar(
                                                        name = it.name,
                                                        avatar = it.avatar + "?v=" + it.avatarVersion,
                                                        modifier = Modifier.align(Alignment.CenterEnd),
                                                        contentPadding = dimensions.small,
                                                        borderColor = MaterialTheme.colorScheme.surface
                                                    )
                                                }
                                            }
                                            Column {
                                                Text(text = match.team1.name, maxLines = 1)
                                                PlayerNamesText(
                                                    p1 = team1ActivePlayers.getOrNull(0)?.name,
                                                    p2 = team1ActivePlayers.getOrNull(1)?.name,
                                                    style = notesTextStyle
                                                )
                                            }
                                        }
                                        TeamSetsRow(
                                            modifier = Modifier.padding(start = dimensions.xSmall),
                                            sets = match.sets.map { it.team1Games },
                                            bestOf = match.rules.bestOf,
                                            currentSetIndex = match.currentSetIndex,
                                            currentGameScore = match.sets.getOrNull(match.currentSetIndex)?.currentGame?.team1Score
                                                ?: Score.Zero,
                                            isPlaying = match.winner == null,
                                            isWinner = match.winner == Side.Team1
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(dimensions.xxSmall),
                                            modifier = Modifier.weight(1f, fill = false)
                                        ) {
                                            val team2ActivePlayers =
                                                match.team2.players.filter { !it.bench }
                                            Box(
                                                modifier = Modifier.height(dimensions.xxLarge)
                                                    .aspectRatio(
                                                        if (match.rules.type == MatchType.Single || team2ActivePlayers.getOrNull(
                                                                1
                                                            ) == null
                                                        ) 1f else 1.5f
                                                    )
                                            ) {
                                                team2ActivePlayers.getOrNull(0)?.let {
                                                    PlayerAvatar(
                                                        name = it.name,
                                                        avatar = it.avatar + "?v=" + it.avatarVersion,
                                                        modifier = Modifier.align(Alignment.CenterStart),
                                                        contentPadding = dimensions.small,
                                                        borderColor = MaterialTheme.colorScheme.surface
                                                    )
                                                }
                                                if (match.rules.type == MatchType.Double)
                                                    team2ActivePlayers.getOrNull(1)?.let {
                                                        PlayerAvatar(
                                                            name = it.name,
                                                            avatar = it.avatar + "?v=" + it.avatarVersion,
                                                            modifier = Modifier.align(Alignment.CenterEnd),
                                                            contentPadding = dimensions.small,
                                                            borderColor = MaterialTheme.colorScheme.surface
                                                        )
                                                    }
                                            }
                                            Column {
                                                Text(text = match.team2.name, maxLines = 1)
                                                PlayerNamesText(
                                                    p1 = team2ActivePlayers.getOrNull(0)?.name,
                                                    p2 = team2ActivePlayers.getOrNull(1)?.name,
                                                    style = notesTextStyle
                                                )
                                            }
                                        }
                                        TeamSetsRow(
                                            modifier = Modifier.padding(start = dimensions.xSmall),
                                            sets = match.sets.map { it.team2Games },
                                            bestOf = match.rules.bestOf,
                                            currentSetIndex = match.currentSetIndex,
                                            currentGameScore = match.sets.getOrNull(match.currentSetIndex)?.currentGame?.team2Score
                                                ?: Score.Zero,
                                            isPlaying = match.winner == null,
                                            isWinner = match.winner == Side.Team2
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun Modifier.shimmerable(
    enabled: Boolean,
    color: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
    shape: Shape = MaterialTheme.shapes.extraSmall,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
): Modifier {
    if (!enabled) return this

    return this
        .padding(paddingValues)
        .shimmer()
        .background(color = color, shape = shape)
        .then(modifier)
}

@Preview
@Composable
fun ProfilePreview() {
    CourtlyTheme(darkTheme = true) {
        ProfileScreen(
            navBack = {},
            profile = Player("", "_mhd_07", "Mohamed", "", "", 0, "", 0),
            followers = emptyList(),
            following = emptyList(),
            result = RemoteResult.Success,
            navToSettings = {},
            myFollowers = emptyList(),
            myFollowing = emptyList(),
            isMine = true,
            previewProfile = {},
            myId = "",
            matches = emptyList(),
            navToMatch = {}
        )
    }
}