package com.mhd_07.courtly.core.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.components.SettingsGroup
import com.mhd_07.courtly.core.presentation.components.SettingsGroupItem
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.edit_profile
import courtly.shared.generated.resources.logout
import courtly.shared.generated.resources.notification
import courtly.shared.generated.resources.privacy
import courtly.shared.generated.resources.profile
import courtly.shared.generated.resources.settings
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.filled.Settings
import dev.seyfarth.tablericons.filled.User
import dev.seyfarth.tablericons.outlined.ChevronRight
import dev.seyfarth.tablericons.outlined.Edit
import dev.seyfarth.tablericons.outlined.Logout
import dev.seyfarth.tablericons.outlined.Notification
import dev.seyfarth.tablericons.outlined.Shield
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(navBack: () -> Unit, profile: Player, logout: () -> Unit, navToEditProfile: () -> Unit) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CourtlyAppBar(
            title = stringResource(Res.string.profile),
            backVisible = true,
            onBackClick = navBack
        )
    }) {
        val dimensions = LocalDimensions.current
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(state = rememberScrollState())
                .padding(horizontal = dimensions.medium, vertical = dimensions.small).padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.small)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
            ) {
                AsyncImage(
                    model = profile.avatar + "?v=" + profile.avatarVersion,
                    contentDescription = stringResource(Res.string.profile),
                    placeholder = rememberVectorPainter(TablerIcons.Filled.User),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth(0.5f).aspectRatio(1f).clip(CircleShape)
                        .border(dimensions.xxSmall, MaterialTheme.colorScheme.primary, CircleShape)
                )
                Text(text = profile.name)
                Text(text = "@${profile.handle}", style = notesTextStyle)
            }
            SettingsGroup(Modifier.fillMaxWidth(), "", {
                SettingsGroupItem(
                    leadingIcon = TablerIcons.Outlined.Edit,
                    title = stringResource(Res.string.edit_profile),
                    trailingIcon = TablerIcons.Outlined.ChevronRight,
                    action = navToEditProfile
                )
            }, {
                SettingsGroupItem(
                    leadingIcon = TablerIcons.Filled.Settings,
                    title = stringResource(Res.string.settings),
                    trailingIcon = TablerIcons.Outlined.ChevronRight,
                    action = {/*TODO: Imlement Screens*/ }
                )
            })
            SettingsGroup(
                Modifier.fillMaxWidth(),
                "",
                {
                    SettingsGroupItem(
                        leadingIcon = TablerIcons.Outlined.Notification,
                        title = stringResource(Res.string.notification),
                        trailingIcon = TablerIcons.Outlined.ChevronRight,
                        action = {/*TODO: Imlement Screens*/ }
                    )
                },
                {
                    SettingsGroupItem(
                        leadingIcon = TablerIcons.Outlined.Shield,
                        title = stringResource(Res.string.privacy),
                        trailingIcon = TablerIcons.Outlined.ChevronRight,
                        action = {/*TODO: Imlement Screens*/ }
                    )
                }
            )
            SettingsGroup(
                Modifier.fillMaxWidth(),
                "",
                {
                    SettingsGroupItem(
                        leadingIcon = TablerIcons.Outlined.Logout,
                        title = stringResource(Res.string.logout),
                        color = MaterialTheme.colorScheme.error,
                        action = logout
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun SettingsPreview() {
    CourtlyTheme(darkTheme = true) {
        SettingsScreen(
            {},
            Player("", "_mhd_07", "Mohamed", "", "",0),
            {},
            {}
        )
    }
}