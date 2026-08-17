package com.mhd_07.courtly.feature_profile_preview.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.presentation.components.AnimatedBottomSheet
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.model.RemoteError
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.normalTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.titleTextStyle
import com.mhd_07.courtly.feature_match_setup.presentation.components.PlayerAvatar
import com.valentinilk.shimmer.shimmer
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.cannot_find_user
import courtly.shared.generated.resources.follow
import courtly.shared.generated.resources.followers
import courtly.shared.generated.resources.follow_you
import courtly.shared.generated.resources.followings
import courtly.shared.generated.resources.map_point_outline
import courtly.shared.generated.resources.menu_dots_outline
import courtly.shared.generated.resources.profile
import courtly.shared.generated.resources.read_less
import courtly.shared.generated.resources.read_more
import courtly.shared.generated.resources.settings
import courtly.shared.generated.resources.settings_outline
import courtly.shared.generated.resources.unfollow
import courtly.shared.generated.resources.user_outline
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
    myId: String?
) {
    val dimensions = LocalDimensions.current
    var followerSheetVisible by remember { mutableStateOf(false) }
    var followingSheetVisible by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CourtlyAppBar(
                title = profile?.name ?: "",
                backVisible = true,
                onBackClick = navBack
            )
        }
    ) { innerPadding ->
        // Followers Bottom Sheet
        PlayerListBottomSheet(
            isVisible = followerSheetVisible,
            title = stringResource(Res.string.followers),
            players = followers,
            onDismissRequest = { followerSheetVisible = false },
            buttonText = { player ->
                println("player ID: ${player.id}, my ID: $myId")
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
            onClick = { player ->
                previewProfile(player)
            }
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
            onClick = { player ->
                previewProfile(player)
            }
        )

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            isRefreshing = (result is RemoteResult.Loading),
            onRefresh = onRefresh
        ) {
            var openMenu by remember { mutableStateOf(false) }

            if (result is RemoteResult.Error && result.error == RemoteError.NotFound && profile == null)
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(Res.string.cannot_find_user))
                }
            else
                Column(modifier = Modifier.fillMaxSize()) {
                    // Banner & Profile Avatar Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Gray)
                        )
                        var avatarSize by remember { mutableStateOf(IntSize.Zero) }

                        SubcomposeAsyncImage(
                            model = "${profile?.avatar ?: ""}?v=${profile?.avatarVersion ?: ""}",
                            contentDescription = stringResource(Res.string.profile),
                            contentScale = ContentScale.Crop,
                            error = {
                                Icon(
                                    painter = painterResource(Res.drawable.user_outline),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(dimensions.xSmall)
                                )
                            },
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = dimensions.small)
                                .fillMaxWidth(0.3f)
                                .aspectRatio(1f)
                                .onSizeChanged { avatarSize = it }
                                .offset { IntOffset(x = 0, y = avatarSize.height / 2) }
                                .clip(CircleShape)
                                .border(
                                    width = dimensions.xxSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
//                            .shimmerable(profile == null)
                        )
                    }

                    // Profile Info Details
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = dimensions.small)
                            .padding(top = dimensions.xSmall, bottom = dimensions.small),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(dimensions.small)
                    ) {
                        // Settings Menu Action
                        if (isMine)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.align(Alignment.End)
                            ) {
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
                                    shape = MaterialTheme.shapes.medium,
//                                modifier = Modifier.padding(dimensions.xSmall)
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
                        else
                            Button(
                                onClick = {
                                    if (profile != null) {
                                        if (myFollowing.contains(profile)) unfollow(profile)
                                        else follow(profile)
                                    }
                                },
                                modifier = Modifier.wrapContentWidth().align(Alignment.End)
                                    .shimmerable(profile == null),
                                colors = ButtonDefaults.buttonColors(containerColor = if (profile == null) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary)
                            ) {
                                if (profile != null)
                                    Text(
                                        text = if (myFollowing.contains(profile)) stringResource(Res.string.unfollow) else stringResource(
                                            Res.string.follow
                                        )
                                    )
                            }

                        // Usernames & Bio
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                        ) {
                            Column {
                                Text(
                                    text = profile?.name ?: "",
                                    style = titleTextStyle,
                                    modifier = Modifier.fillMaxWidth(0.75f)
                                        .shimmerable(profile == null),
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = profile?.let { "@${it.handle}" } ?: "",
                                    style = notesTextStyle,
                                    modifier = Modifier.fillMaxWidth(0.75f).shimmerable(
                                        profile == null,
                                        paddingValues = PaddingValues(vertical = dimensions.xSmall)
                                    ),
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            ExpandableText(
                                text = profile?.bio ?: "",
                                modifier = Modifier.fillMaxWidth()
                                    .shimmerable(profile == null)
                            )
                        }

                        // Location
                        if (profile?.location?.isNotEmpty() == true) {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(dimensions.small),
                                verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.map_point_outline),
                                        contentDescription = null,
                                        tint = Color.Gray
                                    )
                                    Text(text = profile.location, style = notesTextStyle)
                                }
                            }
                        }

                        // Follower Counts
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimensions.small)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall),
                                modifier = Modifier.clickable { followerSheetVisible = true }
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
                                modifier = Modifier.clickable { followingSheetVisible = true }
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerListBottomSheet(
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

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = TextAlign.Justify,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    collapseMaxLines: Int = 3,
    expandMaxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    showMoreText: String = stringResource(Res.string.read_more),
    showLessText: String = stringResource(Res.string.read_less)
) {
    var expanded by remember { mutableStateOf(false) }
    var expandable by remember { mutableStateOf(false) }
    var lastVisibleIndex by remember { mutableStateOf(0) }

    Text(
        text = buildAnnotatedString {
            if (expandable) {
                if (expanded) {
                    append(text.trim())
                    append(" ")
                    withLink(
                        LinkAnnotation.Clickable(
                            tag = "show_less",
                            styles = TextLinkStyles(
                                style = normalTextStyle.copy(
//                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                ).toSpanStyle()
                            )
                        ) {
                            expanded = false
                        }) { append(showLessText) }
                } else {
                    append(
                        text.substring(
                            startIndex = 0,
                            endIndex = lastVisibleIndex - (showMoreText.length + "... ".length)
                        ).trim().trim('.')
                    )
                    append("... ")
                    withLink(
                        LinkAnnotation.Clickable(
                            tag = "show_more",
                            styles = TextLinkStyles(
                                style = normalTextStyle.copy(
//                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                ).toSpanStyle()
                            )
                        ) {
                            expanded = true
                        }) { append(showMoreText) }
                }
            } else append(text.trim())
        },
        modifier = modifier.animateContentSize(),
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        textDecoration = textDecoration,
        textAlign = textAlign,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = if (expanded) expandMaxLines else collapseMaxLines,
        style = style,
        onTextLayout = {
            if (!expanded && it.hasVisualOverflow) {
                expandable = true
                lastVisibleIndex = it.getLineEnd(collapseMaxLines - 1)
            }
        }
    )
}

@Composable
private fun PlayerCard(
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

@Composable
fun Modifier.shimmerable(
    enabled: Boolean,
    color: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
    shape: Shape = MaterialTheme.shapes.extraSmall,
    paddingValues: PaddingValues = PaddingValues(0.dp)
): Modifier {
    if (!enabled) return this

    return this
        .padding(paddingValues)
        .shimmer()
        .background(color = color, shape = shape)
        .drawWithContent {

        }
}

@Preview
@Composable
fun ProfilePreview() {
    CourtlyTheme(darkTheme = true) {
        ProfileScreen(
            navBack = {},
            profile = Player("", "_mhd_07", "Mohamed", "", "", 0),
            followers = emptyList(),
            following = emptyList(),
            result = RemoteResult.Success,
            navToSettings = {},
            myFollowers = emptyList(),
            myFollowing = emptyList(),
            isMine = true,
            previewProfile = {},
            myId = ""
        )
    }
}