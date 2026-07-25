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
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.Plus
import kotlin.collections.listOf

@Composable
fun HomeScreen(navToGameSetup: () -> Unit) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CourtlyAppBar(
            title = "Courtly",
            actions = arrayOf(
                ActionIcon(
                    icon = TablerIcons.Outlined.Plus,
                    contentDescription = "Add", //TODO: Change to stringResource
                    action = navToGameSetup
                )
            )
        )//TODO: Change to stringResource
    }) {
        Box(modifier = Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center) {
            Text("No Saved Games") //TODO: Change to stringResource
        }
    }
}