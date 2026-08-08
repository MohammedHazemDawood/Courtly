package com.mhd_07.courtly.core.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mhd_07.courtly.core.presentation.components.ActionIcon
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.components.Loading
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.empty_feed
import courtly.shared.generated.resources.feed
import courtly.shared.generated.resources.new_game
import courtly.shared.generated.resources.profile
import courtly.shared.generated.resources.my_games
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.Plus
import dev.seyfarth.tablericons.outlined.UserCircle
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(navToGameSetup: () -> Unit, navToProfileScreen: () -> Unit, userPFP: String?) {
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
            startingIcon = userPFP,
            placeHolder = TablerIcons.Outlined.UserCircle,
            startingDescription = stringResource(Res.string.profile),
            onStartingIconClick = navToProfileScreen
        )
    }) {
        val dimensions = LocalDimensions.current
        val pagerState = rememberPagerState { 2 }
        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = dimensions.medium, vertical = dimensions.small).padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
                divider = {},
                containerColor = MaterialTheme.colorScheme.background,
//                indicator = { HorizontalDivider(thickness = dimensions.xxSmall, color = MaterialTheme.colorScheme.primary) },
            ) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                    text = { Text(text = stringResource(Res.string.feed)) },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                )
                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                    text = { Text(text = stringResource(Res.string.my_games)) },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                )
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false,
            ) { page ->
                when (page) {
                    0 -> {
                        //Load Data From Remote
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = stringResource(Res.string.empty_feed))
                        }
                    }

                    1 -> {
                        //Load Data From WorkManager
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    CourtlyTheme(darkTheme = true) {
        HomeScreen(navToGameSetup = {}, navToProfileScreen = {}, userPFP = null)
    }
}