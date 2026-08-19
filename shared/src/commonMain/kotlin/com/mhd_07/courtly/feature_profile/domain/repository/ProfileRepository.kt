package com.mhd_07.courtly.feature_profile.domain.repository

import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.feature_match.domain.model.Match

interface ProfileRepository {
    fun getUserId() : String?
    suspend fun getProfileById(id : String) : Player?
    suspend fun getMyProfile() : Player?
    suspend fun getProfileByHandle(handle : String) : Player?

    suspend fun loadFollowers(userId : String) : List<Player>
    suspend fun loadFollowing(userId : String) : List<Player>
    suspend fun loadMatches(userId : String) : List<Match>
    suspend fun follow(id : String)
    suspend fun unfollow(id : String)

    suspend fun checkHandle(handle : String) : Boolean

    suspend fun updateProfile(profile: Player)

    suspend fun updateAvatar(avatar : ByteArray, currentV : Int)
    suspend fun updateCover(cover : ByteArray, currentV : Int)
    suspend fun logout()

}