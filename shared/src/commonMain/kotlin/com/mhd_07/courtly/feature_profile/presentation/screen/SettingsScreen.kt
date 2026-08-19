package com.mhd_07.courtly.feature_profile.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.components.SettingsGroup
import com.mhd_07.courtly.core.presentation.components.SettingsGroupItem
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.alt_arrow_left_outline
import courtly.shared.generated.resources.alt_arrow_right_outline
import courtly.shared.generated.resources.bell_outline
import courtly.shared.generated.resources.edit_profile
import courtly.shared.generated.resources.logout
import courtly.shared.generated.resources.logout_2_outline
import courtly.shared.generated.resources.notification
import courtly.shared.generated.resources.pen_new_square_outline
import courtly.shared.generated.resources.privacy
import courtly.shared.generated.resources.profile
import courtly.shared.generated.resources.settings
import courtly.shared.generated.resources.settings_outline
import courtly.shared.generated.resources.shield_keyhole_outline
import org.jetbrains.compose.resources.painterResource


import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(navBack: () -> Unit, logout: () -> Unit, navToEditProfile: () -> Unit) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CourtlyAppBar(
            title = stringResource(Res.string.profile),
            backVisible = true,
            onBackClick = navBack
        )
    }) {
        val dimensions = LocalDimensions.current
        val direction = LocalLayoutDirection.current
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(state = rememberScrollState())
                .padding(horizontal = dimensions.medium, vertical = dimensions.small).padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.small)
        ) {
            SettingsGroup(Modifier.fillMaxWidth(), "", {
                SettingsGroupItem(
                    leadingIcon = painterResource(Res.drawable.pen_new_square_outline),
                    title = stringResource(Res.string.edit_profile),
                    trailingIcon = painterResource(if (direction == LayoutDirection.Ltr) Res.drawable.alt_arrow_right_outline else Res.drawable.alt_arrow_left_outline),
                    action = navToEditProfile
                )
            }, {
                SettingsGroupItem(
                    leadingIcon = painterResource(Res.drawable.settings_outline),
                    title = stringResource(Res.string.settings),
                    trailingIcon = painterResource(if (direction == LayoutDirection.Ltr) Res.drawable.alt_arrow_right_outline else Res.drawable.alt_arrow_left_outline),
                    action = {/*TODO: Imlement Screens*/ }
                )
            })
            SettingsGroup(
                Modifier.fillMaxWidth(),
                "",
                {
                    SettingsGroupItem(
                        leadingIcon = painterResource(Res.drawable.bell_outline),
                        title = stringResource(Res.string.notification),
                        trailingIcon = painterResource(if (direction == LayoutDirection.Ltr) Res.drawable.alt_arrow_right_outline else Res.drawable.alt_arrow_left_outline),
                        action = {/*TODO: Imlement Screens*/ }
                    )
                },
                {
                    SettingsGroupItem(
                        leadingIcon = painterResource(Res.drawable.shield_keyhole_outline),
                        title = stringResource(Res.string.privacy),
                        trailingIcon = painterResource(if (direction == LayoutDirection.Ltr) Res.drawable.alt_arrow_right_outline else Res.drawable.alt_arrow_left_outline),
                        action = {/*TODO: Imlement Screens*/ }
                    )
                }
            )
            SettingsGroup(
                Modifier.fillMaxWidth(),
                "",
                {
                    SettingsGroupItem(
                        leadingIcon = painterResource(Res.drawable.logout_2_outline),
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
//            Player("", "_mhd_07", "Mohamed", "", "",0),
            {},
            {}
        )
    }
}