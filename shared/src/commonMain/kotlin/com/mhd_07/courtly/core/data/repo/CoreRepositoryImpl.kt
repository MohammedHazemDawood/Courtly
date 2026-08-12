package com.mhd_07.courtly.core.data.repo

import com.mhd_07.courtly.core.data.mapper.toPlayer
import com.mhd_07.courtly.core.data.model.CREATED_AT
import com.mhd_07.courtly.core.data.model.CheckHandle
import com.mhd_07.courtly.core.data.model.CheckHandleRequest
import com.mhd_07.courtly.core.data.model.CheckHandleResponse
import com.mhd_07.courtly.core.data.model.FOLLOWED
import com.mhd_07.courtly.core.data.model.FOLLOWER
import com.mhd_07.courtly.core.data.model.FOLLOWS
import com.mhd_07.courtly.core.data.model.FollowerResponse
import com.mhd_07.courtly.core.data.model.FollowingResponse
import com.mhd_07.courtly.core.data.model.PROFILES
import com.mhd_07.courtly.core.data.model.PlayerResponse
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.repo.CoreRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import io.ktor.client.call.body
import io.ktor.http.ContentType

class CoreRepositoryImpl(private val client: SupabaseClient) : CoreRepository {
    override suspend fun logout() {
        client.auth.signOut()
    }

    override suspend fun getProfile(): Player? {
        return client.auth.currentUserOrNull()?.let {
            client.postgrest.from(PROFILES).select {
                filter {
                    PlayerResponse::id eq it.id
                }
            }.decodeSingle<PlayerResponse>().toPlayer()
        }
    }

    override suspend fun checkHandle(handle: String): Boolean =
        client.functions.invoke(CheckHandle, body = CheckHandleRequest(handle = handle))
            .body<CheckHandleResponse>().available


    override suspend fun updateProfile(profile: Player) {
        client.postgrest.from(PROFILES).update(
            {
                PlayerResponse::display_name setTo profile.name
                PlayerResponse::avatar_path setTo profile.avatar
                PlayerResponse::handle setTo profile.handle
                PlayerResponse::bio setTo profile.bio
            }
        ) {
            filter {
                PlayerResponse::id eq profile.id
            }
        }
    }

    override suspend fun updateAvatar(avatar: ByteArray, currentV: Int) {
        val bucket = client.storage.from("avatar")
        val path = "${client.auth.currentUserOrNull()?.id}/pfp.jpg"
        bucket.upload(
            path,
            avatar
        ) {
            upsert = true
            contentType = ContentType("image", "jpeg")
        }
        val imageUrl = bucket.publicUrl(path)
        println("Image Url $imageUrl")
        client.auth.currentUserOrNull()?.let {
            println("user: $it")
            client.postgrest.from(PROFILES).update(
                {
                    PlayerResponse::avatar_path setTo imageUrl
                    PlayerResponse::avatar_version setTo currentV + 1
                }
            ) {
                filter {
                    PlayerResponse::id eq it.id
                }
            }
        }
    }

    override suspend fun loadFollowers(): List<Player> {
        val userId = client.auth.currentUserOrNull()?.id ?: return emptyList()

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


    override suspend fun loadFollowing(): List<Player> {
        val userId = client.auth.currentUserOrNull()?.id ?: return emptyList()

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
}