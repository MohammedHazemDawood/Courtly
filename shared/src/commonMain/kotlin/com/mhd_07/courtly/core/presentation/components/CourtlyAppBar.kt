package com.mhd_07.courtly.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.alt_arrow_left_outline
import courtly.shared.generated.resources.alt_arrow_right_outline
import courtly.shared.generated.resources.back
import courtly.shared.generated.resources.user_bold

import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

//add imports


@Composable
fun CourtlyAppBar(
    title: String,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    onBackClick: () -> Unit = {},
    backVisible: Boolean = false,
    dotVisible: Boolean = false,
    vararg actions: ActionIcon,
    //contentPadding: PaddingValues = PaddingValues(horizontal = LocalDimensions.current.medium)
) {
    val dimensions = LocalDimensions.current
    val direction = LocalLayoutDirection.current
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
        //contentPadding = contentPadding,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
            ) {
                Text(text = title, color = titleColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (dotVisible)
                    Box(
                        modifier = Modifier.clip(CircleShape).size(dimensions.small)
                            .background(color = titleColor)
                    )
            }
        },
        navigationIcon = {
            if (backVisible) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(if (direction == LayoutDirection.Ltr) Res.drawable.alt_arrow_left_outline else Res.drawable.alt_arrow_right_outline),
                        contentDescription = stringResource(Res.string.back)
                    )
                }
            }
        },
        actions = {
            actions.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                ) {
                    IconButton(
                        onClick = it.action,
                        enabled = it.enabled
                    ) {
                        Icon(
                            it.icon,
                            contentDescription = it.contentDescription,
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun CourtlyAppBar(
    title: String,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    onBackClick: () -> Unit = {},
    backVisible: Boolean = false,
    dotVisible: Boolean = false,
    trailing: @Composable RowScope.() -> Unit = {},
    //contentPadding: PaddingValues = PaddingValues(horizontal = LocalDimensions.current.medium)
) {
    val dimensions = LocalDimensions.current
    val direction = LocalLayoutDirection.current
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
        //contentPadding = contentPadding,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
            ) {
                Text(text = title, color = titleColor)
                if (dotVisible)
                    Box(
                        modifier = Modifier.clip(CircleShape).size(dimensions.small)
                            .background(color = titleColor)
                    )
            }
        },
        navigationIcon = {
            if (backVisible) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(if (direction == LayoutDirection.Ltr) Res.drawable.alt_arrow_left_outline else Res.drawable.alt_arrow_right_outline),
                        contentDescription = stringResource(Res.string.back)
                    )
                }
            }
        },
        actions = trailing
    )
}


@Composable
fun CourtlyAppBar(
    title: String,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    startingIcon: ImageVector,//TODO: Will Any? on implementing coil
    onStartingIconClick: () -> Unit = {},
    startingDescription: String,
    dotVisible: Boolean = false,
    vararg actions: ActionIcon,
    //contentPadding: PaddingValues = PaddingValues(horizontal = LocalDimensions.current.medium)
) {
    val dimensions = LocalDimensions.current
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
        //contentPadding = contentPadding,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
            ) {
                Text(text = title, color = titleColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (dotVisible)
                    Box(
                        modifier = Modifier.clip(CircleShape).size(dimensions.small)
                            .background(color = titleColor)
                    )
            }
        },
        navigationIcon = {
            IconButton(onClick = onStartingIconClick) {
                Icon(
                    startingIcon,
                    contentDescription = startingDescription
                )
            }
        },
        actions = {
            actions.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                ) {
                    IconButton(
                        onClick = it.action,
                        enabled = it.enabled
                    ) {
                        Icon(
                            it.icon,
                            contentDescription = it.contentDescription,
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun CourtlyAppBar(
    modifier: Modifier = Modifier,
//    startingModifier: Modifier = Modifier,
    title: String,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    startingIcon: Any?,
    placeHolder: Painter,
    onStartingIconClick: () -> Unit = {},
    startingDescription: String,
    dotVisible: Boolean = false,
    vararg actions: ActionIcon,
    //contentPadding: PaddingValues = PaddingValues(horizontal = LocalDimensions.current.medium)
) {
    val dimensions = LocalDimensions.current
    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
        //contentPadding = contentPadding,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
            ) {
                Text(text = title, color = titleColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (dotVisible)
                    Box(
                        modifier = Modifier.clip(CircleShape).size(dimensions.small)
                            .background(color = titleColor)
                    )
            }
        },
        navigationIcon = {
            IconButton(onClick = onStartingIconClick, modifier = modifier) {
                SubcomposeAsyncImage(
                    model = startingIcon,
                    contentDescription = startingDescription,
//                    placeholder = rememberVectorPainter(placeHolder),
                    contentScale = ContentScale.Crop,
                    error = {
                        Box(modifier = Modifier.fillMaxSize().background(Color.LightGray)) {
                            Icon(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(dimensions.xSmall),
                                painter = painterResource(Res.drawable.user_bold),
                                tint = Color.DarkGray,
                                contentDescription = null
                            )
                        }
                    },
                    loading = {
                        Box(modifier = Modifier.fillMaxSize().background(Color.LightGray)) {
                            Icon(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(dimensions.xSmall) ,
                                tint = Color.DarkGray,
                                painter = painterResource(Res.drawable.user_bold),
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.clip(CircleShape)
                        .border(dimensions.xxSmall, MaterialTheme.colorScheme.primary, CircleShape)
                )
            }
        },
        actions = {
            actions.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                ) {
                    IconButton(
                        onClick = it.action,
                        enabled = it.enabled
                    ) {
                        Icon(
                             it.icon,
                            contentDescription = it.contentDescription,
                        )
                    }
                }
            }
        }
    )
}

data class ActionIcon(
    val icon: Painter,
    val contentDescription: String,
    val action: () -> Unit,
    val enabled: Boolean = true
)