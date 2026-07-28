package com.mhd_07.courtly.core.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mhd_07.courtly.core.presentation.components.ActionIcon
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.empty_feed
import courtly.shared.generated.resources.new_game
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.Plus
import dev.seyfarth.tablericons.outlined.UserCircle
import org.jetbrains.compose.resources.stringResource
import kotlin.collections.listOf

@Composable
fun HomeScreen(navToGameSetup: () -> Unit, navToProfileScreen: () -> Unit) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CourtlyAppBar(
            title = "Courtly",
            actions = arrayOf(
                ActionIcon(
                    icon = TablerIcons.Outlined.Plus,
                    contentDescription = stringResource(Res.string.new_game),
                    action = navToGameSetup
                )
            ),
            startingIcon = TablerIcons.Outlined.UserCircle,
            startingDescription = "Profile",
            onStartingIconClick = navToProfileScreen
        )
    }) {
        Box(modifier = Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center) {
            Text(stringResource(Res.string.empty_feed))
        }
    }
}