package com.mhd_07.courtly.core.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.presentation.components.AnimatedBottomSheet
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.titleTextStyle
import com.mhd_07.courtly.feature_match_record.presentation.screen.PlayerAvatar
import com.mhd_07.courtly.feature_match_record.presentation.screen.PlayerRowContent
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.follow
import courtly.shared.generated.resources.followers
import courtly.shared.generated.resources.following_each_other
import courtly.shared.generated.resources.followings
import courtly.shared.generated.resources.map_point_outline
import courtly.shared.generated.resources.menu_dots_circle_outline
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
    profile: Player,
    followers: List<Player>,
    following: List<Player>,
    result: RemoteResult?,
    onRefresh: () -> Unit = {},
//    logout: () -> Unit,
    navToSettings: () -> Unit,
    follow: (Player) -> Unit = {},
    unfollow: (Player) -> Unit = {}
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CourtlyAppBar(
            title = profile.name,//stringResource(Res.string.profile),
            backVisible = true,
            onBackClick = navBack
        )
    }) {
        val dimensions = LocalDimensions.current
        var followerSheetVisible by remember { mutableStateOf(false) }
        var followingSheetVisible by remember { mutableStateOf(false) }

        AnimatedBottomSheet(
            isVisible = followerSheetVisible,
            onDismissRequest = { followerSheetVisible = false },
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.4f)
                    .padding(horizontal = dimensions.small).padding(bottom = dimensions.small),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {
                Text(text = stringResource(Res.string.followers))
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                ) {
                    items(followers.size) {
                        if (it != 0)
                            VerticalDivider()
                        PlayerCard(
                            player = followers[it],
                            buttonText = if (following.contains(followers[it])) stringResource(Res.string.unfollow) else stringResource(
                                Res.string.follow
                            ),
                            badgeText = if (following.contains(followers[it])) stringResource(Res.string.following_each_other) else null,
                            onClick = {
                                if (following.contains(followers[it]))
                                    unfollow(followers[it])
                                else
                                    follow(followers[it])
                                followerSheetVisible = false
                            })
                    }
                }
            }
        }
        AnimatedBottomSheet(
            isVisible = followingSheetVisible,
            onDismissRequest = { followingSheetVisible = false },
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.4f)
                    .padding(horizontal = dimensions.small).padding(bottom = dimensions.small),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {
                Text(text = stringResource(Res.string.followings))
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                ) {
                    items(following.size) {
                        if (it != 0)
                            VerticalDivider()
                        PlayerCard(
                            player = following[it],
                            buttonText = stringResource(Res.string.unfollow),
                            badgeText = if (followers.contains(following[it])) stringResource(Res.string.following_each_other) else null,
                            onClick = {
                                unfollow(following[it])
                                followerSheetVisible = false
                            })
                    }
                }
            }
        }
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            isRefreshing = result == RemoteResult.Loading,
            onRefresh = onRefresh
        ) {
            var openMenu by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier.fillMaxSize()
//                    .verticalScroll(state = rememberScrollState())
                    .padding(it),
                horizontalAlignment = Alignment.Start,
//                verticalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {

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
                        model = "${profile.avatar}?v=${profile.avatarVersion}",
                        contentDescription = stringResource(Res.string.profile),
                        contentScale = ContentScale.Crop,
                        error = {
                            Icon(
                                painter = painterResource(Res.drawable.user_outline),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().padding(dimensions.xSmall)
                            )
                        },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = dimensions.small)
                            .fillMaxWidth(0.3f)
                            .aspectRatio(1f)
                            .onSizeChanged { avatarSize = it }
                            .offset {
                                IntOffset(
                                    x = 0,
                                    y = avatarSize.height / 2
                                )
                            }
                            .clip(CircleShape)
                            .border(
                                dimensions.xxSmall,
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            )
                    )
                }
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                        .padding(horizontal = dimensions.small)
                        .padding(top = dimensions.xSmall, bottom = dimensions.small),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(dimensions.small)
                ) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(dimensions.xSmall),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        IconButton(
                            onClick = { openMenu = true },
                            modifier = Modifier.border(dimensions.xxSmall, Color.Gray, CircleShape)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.menu_dots_outline),
                                contentDescription = stringResource(Res.string.settings)
                            )
                        }
                        DropdownMenu(
                            expanded = openMenu,
                            onDismissRequest = { openMenu = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(Res.drawable.settings_outline),
                                        null
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
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                    ) {
                        Column {
                            Text(text = profile.name, style = titleTextStyle)
                            Text(text = "@${profile.handle}", style = notesTextStyle)
                        }
                        ReadMoreText(
                            modifier = Modifier.fillMaxWidth(),
                            text = profile.bio,
//                            style = notesTextStyle
                        )
                    }
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensions.small),
                        verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                    ) {
                        if (profile.location.isNotEmpty())
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensions.small)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall),
                            modifier = Modifier.clickable { followerSheetVisible = true }
                        ) {
                            Text(text = followers.count().toString())
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
                            Text(text = following.count().toString())
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

@Preview
@Composable
fun ProfilePreview() {
    CourtlyTheme(darkTheme = true) {
        ProfileScreen(
            {},
            Player("", "_mhd_07", "Mohamed", "", "", 0),
            emptyList(),
            emptyList(),
            RemoteResult.Success,
            {},
            {},
//            {}
        )
    }
}

@Composable
fun ReadMoreText(
    text: String,
    modifier: Modifier = Modifier,
    defaultMaxLines: Int = 3,
    expandedMaxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    readLess: String = stringResource(Res.string.read_less),
    readMore: String = stringResource(Res.string.read_more),
    actionSpanStyle: SpanStyle = SpanStyle(
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
) {
    var expanded by remember(text) { mutableStateOf(false) }
    var cutIndex by remember(text) { mutableStateOf<Int?>(null) }

    val finalText = buildAnnotatedString {
        if (expanded) {
            append(text)
            append(" ")

            val start = length
            append(readLess)

            addStyle(
                style = actionSpanStyle,
                start = start,
                end = length
            )

            addLink(
                clickable = LinkAnnotation.Clickable(
                    tag = "read_less",
                    linkInteractionListener = {
                        expanded = false
                    }
                ),
                start = start,
                end = length
            )
        } else if (cutIndex != null) {
            append(text.substring(0, cutIndex!!).trimEnd())
            append("… ")

            val start = length
            append(readMore)

            addStyle(
                style = actionSpanStyle,
                start = start,
                end = length
            )

            addLink(
                clickable = LinkAnnotation.Clickable(
                    tag = "read_more",
                    linkInteractionListener = {
                        expanded = true
                    }
                ),
                start = start,
                end = length
            )
        } else {
            append(text)
        }
    }

    Text(
        text = finalText,
        modifier = modifier.animateContentSize(),
        maxLines = if (expanded) expandedMaxLines else defaultMaxLines,
        style = style,
        onTextLayout = { layoutResult ->

            if (
                !expanded &&
                cutIndex == null &&
                layoutResult.hasVisualOverflow
            ) {
                val lastLine = defaultMaxLines - 1

                val lineStart = layoutResult.getLineStart(lastLine)
                val lineEnd = layoutResult.getLineEnd(
                    lineIndex = lastLine,
                    visibleEnd = true
                )

                // Keep some room for "... Read More"
                val suffix = "… $readMore"

                // Start by removing enough characters for the suffix.
                var candidate = (lineEnd - suffix.length)
                    .coerceAtLeast(lineStart)

                // Make sure the candidate doesn't end in the middle
                // of an awkward whitespace sequence.
                while (
                    candidate > lineStart &&
                    text.getOrNull(candidate - 1)?.isWhitespace() == true
                ) {
                    candidate--
                }

                cutIndex = candidate
            }
        }
    )
}

@Composable
private fun PlayerCard(
    player: Player,
    modifier: Modifier = Modifier,
    buttonText: String,
    badgeText: String? = null,
    onClick: () -> Unit
) {
//    val dimensions = LocalDimensions.current

//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .border(
//                width = dimensions.xxSmall,
//                color = Color.Gray,
//                shape = MaterialTheme.shapes.medium
//            )
//    ) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PlayerRowContent(/*modifier = Modifier.weight(2f),*/ player = player, badgeText = badgeText)
        Button(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = buttonText)
        }
    }
//    }
}

@Composable
fun PlayerRowContent(
    modifier: Modifier = Modifier,
    player: Player,
    badgeText: String? = null,
) {
    val dimensions = LocalDimensions.current

    Row(
        modifier = modifier/*.height(IntrinsicSize.Min)*/,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
    ) {
        PlayerAvatar(avatarUrl = player.avatar)
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
            ) {
                Text(text = player.name)
                badgeText?.let {
                    Badge {
                        Text(text = it, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
            player.handle?.let {
                Text(text = "@$it", style = notesTextStyle, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}