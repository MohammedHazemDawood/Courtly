package com.mhd_07.courtly.feature_profile.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FollowerResponse(
    val follower: String,
//    val created_at: Instant
)

@Serializable
data class FollowingResponse(
    val followed: String,
//    val created_at: Instant
)
@Serializable
data class FollowRequest(
    val follower: String,
    val followed: String
)

/*

data class FollowingResponse(
    val followed: String,
    val createdAt: Instant
)*/
