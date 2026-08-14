package com.mhd_07.courtly.core.domain.repo

import com.mhd_07.courtly.core.domain.model.Player

interface CoreRepository {
    suspend fun logout()
    suspend fun getProfile() : Player?

    suspend fun checkHandle(handle : String) : Boolean

    suspend fun updateProfile(profile: Player)

    suspend fun updateAvatar(avatar : ByteArray, currentV : Int)

    suspend fun loadFollowers() : List<Player>
    suspend fun loadFollowing() : List<Player>

    suspend fun follow(id : String)
    suspend fun unfollow(id : String)
}