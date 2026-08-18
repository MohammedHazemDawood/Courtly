package com.mhd_07.courtly.core.domain.model

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class Player(
    val id : String =  Uuid.generateV7().toString(),
    val handle : String?,
    val name : String,
    val avatar : String?,
    val cover : String?,
    val coverVersion : Int,
    val bio : String,
    val avatarVersion : Int,
    val bench : Boolean = true,
    val visibility: Visibility = Visibility.Public,
    val location : String = "",
    val isRemote : Boolean = true
)
