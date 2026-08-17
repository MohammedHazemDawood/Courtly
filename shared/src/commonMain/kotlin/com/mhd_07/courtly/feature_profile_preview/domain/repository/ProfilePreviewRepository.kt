package com.mhd_07.courtly.feature_profile_preview.domain.repository

import com.mhd_07.courtly.core.domain.model.Player

interface ProfilePreviewRepository {
    fun getUserId() : String?
    suspend fun getProfileById(id : String) : Player?
    suspend fun getProfileByHandle(handle : String) : Player?

    suspend fun loadFollowers(userId : String) : List<Player>
    suspend fun loadFollowing(userId : String) : List<Player>

    suspend fun follow(id : String)
    suspend fun unfollow(id : String)
}