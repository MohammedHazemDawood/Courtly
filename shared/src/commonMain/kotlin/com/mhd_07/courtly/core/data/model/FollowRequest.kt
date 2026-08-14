package com.mhd_07.courtly.core.data.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

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
