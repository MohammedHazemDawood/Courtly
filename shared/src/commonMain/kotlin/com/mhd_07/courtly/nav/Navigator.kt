package com.mhd_07.courtly.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.rememberViewModelStoreOwner
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.mhd_07.courtly.core.presentation.screens.HomeScreen
import com.mhd_07.courtly.feature_match_record.presentation.screen.MatchScreen
import com.mhd_07.courtly.feature_match_record.presentation.screen.MatchSetupScreen
import com.mhd_07.courtly.feature_match_record.presentation.viewmodel.MatchIntent
import com.mhd_07.courtly.feature_match_record.presentation.viewmodel.MatchViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigator() {
    val backStack = rememberNavBackStack(configuration = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(Routes.Home::class, Routes.Home.serializer())
                subclass(Routes.GameSetup::class, Routes.GameSetup.serializer())
                subclass(Routes.GameRecord::class, Routes.GameRecord.serializer())
            }
        }
    }, Routes.Home)

//    val owner = rememberViewModelStoreOwner()
    val matchViewModel: MatchViewModel = koinViewModel(/*viewModelStoreOwner = owner*/)
    val state = matchViewModel.state.collectAsStateWithLifecycle()
    val undoAvailable = matchViewModel.isUndoAvailable.collectAsStateWithLifecycle()
    val redoAvailable = matchViewModel.isRedoAvailable.collectAsStateWithLifecycle()
    LaunchedEffect(state.value) {
        println("Navigator: state=${ state.value }")
    }
    NavDisplay(
        backStack = backStack,
        modifier = Modifier.fillMaxSize(),
        entryDecorators = listOf(
            // Required for saving Compose state per entry
            rememberSaveableStateHolderNavEntryDecorator(),
            // Required for ViewModel scoping per entry
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = {
            when (it) {
                is Routes.Home -> NavEntry(
                    key = Routes.Home,
                    content = { HomeScreen { backStack.add(Routes.GameSetup) } })

                is Routes.GameSetup -> NavEntry(
                    key = Routes.GameSetup,
                    content = {
                        MatchSetupScreen(
                            state.value,
                            navToGameRecord = { backStack.add(Routes.GameRecord) },
                            navBack = {
                                if (backStack.size > 1)
                                    backStack.removeLastOrNull()
                            },
                            onChangeName = { side, name ->
                                matchViewModel.handleIntent(
                                    MatchIntent.EditTeamName(
                                        side,
                                        name
                                    )
                                )
                            },
                            onEditLocation = { location ->
                                matchViewModel.handleIntent(
                                    MatchIntent.EditLocation(
                                        location
                                    )
                                )
                            },
                            onModeChange = { mode ->
                                matchViewModel.handleIntent(
                                    MatchIntent.EditMode(
                                        mode
                                    )
                                )
                            },
                            onBestOfChange = { bestOf ->
                                matchViewModel.handleIntent(
                                    MatchIntent.EditBestOf(
                                        bestOf
                                    )
                                )
                            },
                            onTypeChange = { type ->
                                matchViewModel.handleIntent(
                                    MatchIntent.EditType(
                                        type
                                    )
                                )
                            },
                            startGame = { side ->
                                matchViewModel.handleIntent(
                                    MatchIntent.StartGame(
                                        side
                                    )
                                )
                            }
                        )
                    })

                is Routes.GameRecord -> NavEntry(
                    key = Routes.GameRecord,
                    content = {
                        MatchScreen(
                            state.value,
                            navBack = {
                                if (backStack.size > 1) {
                                    matchViewModel.clearVM()
                                    backStack.removeLastOrNull()
                                    backStack.removeLastOrNull()
                                }
                            },
                            undoAvailable = undoAvailable.value,
                            redoAvailable = redoAvailable.value,
                            onUndo = { matchViewModel.handleIntent(MatchIntent.Undo) },
                            onRedo = { matchViewModel.handleIntent(MatchIntent.Redo) },
                            onPoint = { side -> matchViewModel.handleIntent(MatchIntent.Point(side)) }
                        )
                    })

                else -> error("Unknown route: $it")
            }
        })
}