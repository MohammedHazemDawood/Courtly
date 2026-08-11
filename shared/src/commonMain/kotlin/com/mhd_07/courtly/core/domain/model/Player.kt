package com.mhd_07.courtly.core.domain.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Player(
    val id : String =  Uuid.generateV7().toString(),
    val handle : String?,
    val name : String,
    val avatar : String?,
    val bio : String,
    val avatarVersion : Int,
    val bench : Boolean = false,
)
