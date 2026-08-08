package com.mhd_07.courtly.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.back
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ChevronLeft
import dev.seyfarth.tablericons.outlined.ChevronRight
import org.jetbrains.compose.resources.stringResource

//add imports


@Composable
fun CourtlyAppBar(
    title: String,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    onBackClick: () -> Unit = {},
    backVisible: Boolean = false,
    dotVisible: Boolean = false,
    vararg actions: ActionIcon
) {
    val dimensions = LocalDimensions.current
    val direction = LocalLayoutDirection.current
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
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
                        imageVector = if (direction == LayoutDirection.Ltr) TablerIcons.Outlined.ChevronLeft else TablerIcons.Outlined.ChevronRight,
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
                            imageVector = it.icon,
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
    startingIcon: ImageVector,//TODO: Will Any? on implementing coil
    onStartingIconClick: () -> Unit = {},
    startingDescription: String,
    dotVisible: Boolean = false,
    vararg actions: ActionIcon,
) {
    val dimensions = LocalDimensions.current
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
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
            IconButton(onClick = onStartingIconClick) {
                Icon(
                    imageVector = startingIcon,
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
                            imageVector = it.icon,
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
    startingIcon: Any?,
    placeHolder: ImageVector,
    onStartingIconClick: () -> Unit = {},
    startingDescription: String,
    dotVisible: Boolean = false,
    vararg actions: ActionIcon,
) {
    val dimensions = LocalDimensions.current
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
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
            IconButton(onClick = onStartingIconClick) {
                AsyncImage(
                    model = startingIcon,
                    contentDescription = startingDescription,
                    placeholder = rememberVectorPainter(placeHolder),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape).border(dimensions.xxSmall, MaterialTheme.colorScheme.primary, CircleShape)
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
                            imageVector = it.icon,
                            contentDescription = it.contentDescription,
                        )
                    }
                }
            }
        }
    )
}

data class ActionIcon(
    val icon: ImageVector,
    val contentDescription: String,
    val action: () -> Unit,
    val enabled: Boolean = true
)