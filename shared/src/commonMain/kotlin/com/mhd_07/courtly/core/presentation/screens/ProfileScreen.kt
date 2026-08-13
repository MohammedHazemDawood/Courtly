package com.mhd_07.courtly.core.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.titleTextStyle
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.followers
import courtly.shared.generated.resources.followings
import courtly.shared.generated.resources.map_point_outline
import courtly.shared.generated.resources.menu_dots_circle_outline
import courtly.shared.generated.resources.menu_dots_outline
import courtly.shared.generated.resources.profile
import courtly.shared.generated.resources.read_less
import courtly.shared.generated.resources.read_more
import courtly.shared.generated.resources.settings
import courtly.shared.generated.resources.settings_outline
import courtly.shared.generated.resources.user_outline
import org.jetbrains.compose.resources.painterResource


import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileScreen(
    navBack: () -> Unit,
    profile: Player,
    followers: List<Player>,
    following: List<Player>,
    result: RemoteResult?,
    onRefresh: () -> Unit = {},
//    logout: () -> Unit,
    navToSettings: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CourtlyAppBar(
            title = profile.name,//stringResource(Res.string.profile),
            backVisible = true,
            onBackClick = navBack
        )
    }) {
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            isRefreshing = result == RemoteResult.Loading,
            onRefresh = onRefresh
        ) {
            val dimensions = LocalDimensions.current
            var openMenu by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier.fillMaxSize()
//                    .verticalScroll(state = rememberScrollState())
                    .padding(it),
                horizontalAlignment = Alignment.Start,
//                verticalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {
                var avatarSize by remember { mutableStateOf(IntSize.Zero) }

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
                            .padding(start = dimensions.medium)
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
                        .padding(horizontal = dimensions.medium)
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
                            horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                        ) {
                            Text(text = followers.count().toString())
                            Text(
                                text = stringResource(Res.string.followers),
                                style = notesTextStyle
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
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
    modifier: Modifier = Modifier,
    text: String,
    defaultMaxLines: Int = 3,
    expandedMaxLines: Int = 6,
    style: TextStyle = LocalTextStyle.current,
    readLess: String = stringResource(Res.string.read_less),
    readMore: String = stringResource(Res.string.read_more)
) {
    var expanded by remember { mutableStateOf(false) }

    var displayText by remember(text, expanded) {
        mutableStateOf(
            AnnotatedString(text)
        )
    }

    Text(
        modifier = modifier.animateContentSize(),
        text = displayText,
        maxLines = if (expanded) expandedMaxLines else defaultMaxLines,
        overflow = TextOverflow.Ellipsis,
        style = style,
        onTextLayout = { result ->

            if (expanded) {
                displayText = buildAnnotatedString {
                    append(text)
                    append(" ...$readLess")

                    addLink(
                        clickable = LinkAnnotation.Clickable(
                            tag = "read_less",
                            linkInteractionListener = {
                                expanded = false
                            }
                        ),
                        start = text.length + 1,
                        end = text.length + 1 + readLess.length + 3
                    )
                }

                return@Text
            }

            if (!result.hasVisualOverflow) {
                displayText = AnnotatedString(text)
                return@Text
            }

            val lastLine = result.lineCount - 1
            val lineStart = result.getLineStart(lastLine)
            val lineEnd = result.getLineEnd(lastLine)

            val availableText = text
                .substring(0, lineEnd)
                .trimEnd()

            displayText = buildAnnotatedString {
                append(availableText)
                append("...")

                withLink(
                    LinkAnnotation.Clickable(
                        tag = "read_more",
                        linkInteractionListener = {
                            expanded = true
                        }
                    )
                ) {
                    append(readMore)
                }
            }
        }
    )
}