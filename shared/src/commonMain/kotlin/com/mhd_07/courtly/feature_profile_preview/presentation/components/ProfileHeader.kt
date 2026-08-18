package com.mhd_07.courtly.feature_profile_preview.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import coil3.compose.SubcomposeAsyncImage
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.feature_profile_preview.presentation.screen.shimmerable
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.alt_arrow_left_outline
import courtly.shared.generated.resources.alt_arrow_right_outline
import courtly.shared.generated.resources.follow_you
import courtly.shared.generated.resources.user_bold
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMotionApi::class)
@Composable
fun ProfileHeader(
    progress: Float,
    name: String?,
    handle: String?,
    avatar: String?,
    cover: String?,
    action: @Composable () -> Unit,
    onBack: () -> Unit,
    isFollowing: Boolean
) {
    val dimensions = LocalDimensions.current
    val direction = LocalLayoutDirection.current
    val backIcon =
        if (direction == LayoutDirection.Ltr) Res.drawable.alt_arrow_left_outline else Res.drawable.alt_arrow_right_outline
    var json: String? by remember { mutableStateOf(null) }
    LaunchedEffect(Unit) {
        json = Res.readBytes(
            "files/profile_motion.json5"
        ).decodeToString()
    }
    val additionalModifier = if (progress != 1f) Modifier.padding(
        top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    ) else Modifier

    json?.let {
        MotionLayout(
            motionScene = MotionScene(content = it),
            progress = progress
        ) {
            SubcomposeAsyncImage(
                model = cover,
                contentDescription = "Cover Banner",
                contentScale = ContentScale.Crop,
                error = {
                    Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray))
                },
                loading = {
                    Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray))
                },
                modifier = Modifier
                    .layoutId("banner")
                    .blur(radius = (progress * 16).dp)
            )

            // 2. Avatar / Profile Picture
            SubcomposeAsyncImage(
                model = avatar,
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                error = {
                    Box(modifier = Modifier.fillMaxSize().background(Color.LightGray)) {
                        Icon(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(dimensions.small),
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
                                .padding(dimensions.small),
                            tint = Color.DarkGray,
                            painter = painterResource(Res.drawable.user_bold),
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier
                    .layoutId("pfp")
                    .clip(CircleShape)
                    .border(
                        dimensions.xxSmall,
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    )
            )

            // 3. Back Button
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .layoutId("back_btn")
                    .then(additionalModifier)
            ) {
                Icon(
                    painter = painterResource(backIcon),
                    contentDescription = "Back",
                )
            }

            Box(modifier = Modifier.layoutId("name").shimmerable(enabled = name == null)) {
                Text(
                    text = name ?: "",
                    maxLines = 2,
                    modifier = Modifier
                        .layoutId("name")
                )
                if (name == null)
                    Text(
                        text = "Any Fucking Name",
                        modifier = Modifier.visible(false)
                    )
            }

            Box(
                modifier = Modifier.layoutId("handle")/*.fillMaxWidth(0.5f)*/
                    .shimmerable(enabled = handle == null)
            ) {
                Text(
                    text = handle?.let { "@$handle" } ?: "",
                    style = notesTextStyle,
                )
                if (handle == null)
                    Text(
                        text = "Any Handle",
                        modifier = Modifier.visible(false)
                    )
            }

            if (isFollowing)
                Badge(
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.layoutId("badge")
                ) {
                    Text(
                        text = stringResource(Res.string.follow_you),
                        modifier = Modifier.padding(dimensions.xxSmall)
                    )
                }

            Box(
                modifier = Modifier
                    .layoutId("action"),
//                    .then(additionalModifier),
                contentAlignment = Alignment.CenterEnd
            ) {
                action()
            }
        }
    }
}