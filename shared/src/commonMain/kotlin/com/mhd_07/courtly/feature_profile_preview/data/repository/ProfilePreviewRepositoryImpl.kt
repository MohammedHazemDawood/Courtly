package com.mhd_07.courtly.feature_profile_preview.data.repository

import com.mhd_07.courtly.core.data.mapper.toPlayer
import com.mhd_07.courtly.feature_profile_preview.data.model.FOLLOWED
import com.mhd_07.courtly.feature_profile_preview.data.model.FOLLOWER
import com.mhd_07.courtly.core.data.model.FOLLOWS
import com.mhd_07.courtly.feature_profile_preview.data.model.FollowRequest
import com.mhd_07.courtly.feature_profile_preview.data.model.FollowerResponse
import com.mhd_07.courtly.feature_profile_preview.data.model.FollowingResponse
import com.mhd_07.courtly.core.data.model.PROFILES
import com.mhd_07.courtly.core.data.model.PlayerResponse
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.feature_profile_preview.domain.repository.ProfilePreviewRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.exceptions.NotFoundRestException
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

class ProfilePreviewRepositoryImpl(private val client: SupabaseClient) : ProfilePreviewRepository {
    override fun getUserId(): String? {
        return client.auth.currentUserOrNull()?.id
    }

    override suspend fun getProfileById(id : String): Player {
//        return client.auth.currentUserOrNull()?.let {
        return client.postgrest.from(PROFILES).select {
            filter {
                PlayerResponse::id eq id
            }
        }.decodeSingle<PlayerResponse?>()?.toPlayer() ?: throw Exception("User Not Found")
//        }
    }
    override suspend fun getProfileByHandle(handle : String): Player {
//        return client.auth.currentUserOrNull()?.let {
        return client.postgrest.from(PROFILES).select {
            filter {
                PlayerResponse::handle eq handle
            }
        }.decodeSingle<PlayerResponse?>()?.toPlayer() ?: throw Exception("User Not Found")
//        }
    }

    override suspend fun loadFollowers(userId: String): List<Player> {
//        val userId = client.auth.currentUserOrNull()?.id ?: return emptyList()

        println("my id: $userId")

        val followerIds = client.postgrest
            .from(FOLLOWS)
            .select(columns = Columns.list(FOLLOWER)) {
                filter {
                    eq(FOLLOWED, userId)
                }
            }
            .decodeList<FollowerResponse>()
            .also {
                println("Followers: $it")
            }
            .map(FollowerResponse::follower)

        if (followerIds.isEmpty()) return emptyList()

        return client.postgrest
            .from(PROFILES)
            .select {
                filter {
                    PlayerResponse::id isIn followerIds
                }
            }
            .decodeList<PlayerResponse>()
            .map { it.toPlayer() }
    }


    override suspend fun loadFollowing(userId: String): List<Player> {
//        val userId = client.auth.currentUserOrNull()?.id ?: return emptyList()

        println("my id: $userId")

        val followerIds = client.postgrest
            .from(FOLLOWS)
            .select(columns = Columns.list(FOLLOWED)) {
                filter {
                    eq(FOLLOWER, userId)
                }
            }
            .decodeList<FollowingResponse>()
            .also {
                println("Following: $it")
            }
            .map(FollowingResponse::followed)

        if (followerIds.isEmpty()) return emptyList()

        return client.postgrest
            .from(PROFILES)
            .select {
                filter {
                    PlayerResponse::id isIn followerIds
                }
            }
            .decodeList<PlayerResponse>()
            .map { it.toPlayer() }
    }

    override suspend fun follow(id: String) {
        val user = client.auth.currentUserOrNull() ?: return
        if (id == user.id) return
        val follow = FollowRequest(follower = user.id, followed = id)
        client.postgrest.from(FOLLOWS).insert(follow)
    }

    override suspend fun unfollow(id: String) {
        val user = client.auth.currentUserOrNull() ?: return
        client.postgrest.from(FOLLOWS)
            .delete {
                filter {
                    and {
                        eq(FOLLOWER, user.id)
                        eq(FOLLOWED, id)
                    }
                }
            }
    }
}