package com.mhd_07.courtly.feature_match_setup.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.core.presentation.model.RemoteError
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.core.presentation.model.getPostgrestError
import com.mhd_07.courtly.feature_match_setup.domain.usecase.*
import com.mhd_07.courtly.feature_match_setup.presentation.model.MatchSetupIntent
import com.mhd_07.courtly.feature_match_setup.presentation.model.MatchSetupState
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MatchSetupViewModel(
    private val setupMatch: SetupUseCase,
    private val searchUser: SearchPlayerUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MatchSetupState())
    var searchQuery = MutableStateFlow("")


    val state = combine(_state, searchQuery) { state, query ->
        state.copy(searchQuery = query)
    }.stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5000), MatchSetupState())

    init {
        viewModelScope.launch {
            searchQuery.debounce(1000.milliseconds).collectLatest {
                _state.value = _state.value.copy(searchQuery = it)
                if (it.trim().isEmpty()) return@collectLatest
                try {
                    _state.update { it.copy(result = RemoteResult.Loading) }
                    val result = searchUser(it)
                    _state.update { it.copy(result = RemoteResult.Success, searchResults = result) }
                } catch (e: PostgrestRestException) {
                    _state.update { it.copy(result = RemoteResult.Error(getPostgrestError(e.code))) }
                } catch (e: Exception) {
                    _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
                    println("Searching Error = $e")
                }
            }
        }
    }

    fun handleIntent(intent: MatchSetupIntent) {
        _state.value.setup.let { setup ->
            when (intent) {
                is MatchSetupIntent.AddTeamLeftPlayer -> {
                    if (setup.teamRight.players.contains(intent.player) &&
                        setup.teamLeft.players.contains(intent.player)
                    )
                        return@let
                    _state.update {
                        it.copy(
                            setup =
                                setup.copy(
                                    teamLeft = setup.teamLeft.copy(
                                        players = setup.teamLeft.players + intent.player
                                    )
                                )
                        )
                    }
                }

                is MatchSetupIntent.AddTeamRightPlayer -> {
                    if (setup.teamRight.players.contains(intent.player) &&
                        setup.teamLeft.players.contains(intent.player)
                    )
                        return@let
                    _state.update {
                        it.copy(
                            setup =
                                setup.copy(
                                    teamRight = setup.teamRight.copy(
                                        players = setup.teamRight.players + intent.player
                                    )
                                )
                        )
                    }
                }

                is MatchSetupIntent.ChangeBestOf -> _state.update {
                    it.copy(
                        setup = setup.copy(
                            bestOf = intent.newBestOf
                        )
                    )
                }

                is MatchSetupIntent.ChangeLocation -> _state.update {
                    it.copy(
                        setup = setup.copy(
                            location = intent.newLocation
                        )
                    )
                }

                is MatchSetupIntent.ChangeMode -> _state.update { it.copy(setup = setup.copy(mode = intent.newMode)) }
                is MatchSetupIntent.ChangeTeamLeftName -> _state.update {
                    it.copy(
                        setup = setup.copy(
                            teamLeft = setup.teamLeft.copy(name = intent.newName)
                        )
                    )
                }

                is MatchSetupIntent.ChangeTeamRightName -> _state.update {
                    it.copy(
                        setup = setup.copy(
                            teamRight = setup.teamRight.copy(name = intent.newName)
                        )
                    )
                }

                is MatchSetupIntent.ChangeType -> _state.update { it.copy(setup = setup.copy(type = intent.newType)) }
                is MatchSetupIntent.RemoveTeamLeftPlayer -> _state.update {
                    it.copy(
                        setup =
                            setup.copy(
                                teamLeft = setup.teamLeft.copy(
                                    players = setup.teamLeft.players - intent.player
                                )
                            )
                    )
                }

                is MatchSetupIntent.RemoveTeamRightPlayer -> {
                    _state.update {
                        it.copy(
                            setup =
                                setup.copy(
                                    teamRight = setup.teamRight.copy(
                                        players = setup.teamRight.players - intent.player
                                    )
                                )
                        )
                    }
                }

                is MatchSetupIntent.SearchPlayers -> searchQuery.update { intent.query }
                MatchSetupIntent.SetupMatch -> viewModelScope.launch {
                    if (_state.value.matchId == null)
                    try {
                        _state.update { it.copy(result = RemoteResult.Loading) }
                        val result = setupMatch(state.value.setup)
                        _state.update { it.copy(result = RemoteResult.Success, matchId = result) }
                    } catch (e: PostgrestRestException) {
                        _state.update { it.copy(result = RemoteResult.Error(getPostgrestError(e.code))) }
                    } catch (e: Exception) {
                        _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
                        println("Insert to database Error = $e")
                    }
                }
            }
        }
    }
}