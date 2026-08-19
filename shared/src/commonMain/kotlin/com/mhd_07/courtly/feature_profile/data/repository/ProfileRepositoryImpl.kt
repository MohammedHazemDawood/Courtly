package com.mhd_07.courtly.feature_profile.data.repository

import com.mhd_07.courtly.core.data.mapper.toPlayer
import com.mhd_07.courtly.core.data.model.CheckHandle
import com.mhd_07.courtly.core.data.model.CheckHandleRequest
import com.mhd_07.courtly.core.data.model.CheckHandleResponse
import com.mhd_07.courtly.feature_profile.data.model.FOLLOWED
import com.mhd_07.courtly.feature_profile.data.model.FOLLOWER
import com.mhd_07.courtly.core.data.model.FOLLOWS
import com.mhd_07.courtly.core.data.model.MATCHES
import com.mhd_07.courtly.feature_profile.data.model.FollowRequest
import com.mhd_07.courtly.feature_profile.data.model.FollowerResponse
import com.mhd_07.courtly.feature_profile.data.model.FollowingResponse
import com.mhd_07.courtly.core.data.model.PROFILES
import com.mhd_07.courtly.core.data.model.PlayerResponse
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.feature_match.data.mapper.toMatch
import com.mhd_07.courtly.feature_match.data.model.RemoteMatch
import com.mhd_07.courtly.feature_match.domain.model.Match
import com.mhd_07.courtly.feature_profile.domain.repository.ProfileRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage
import io.ktor.client.call.body
import io.ktor.http.ContentType

class ProfileRepositoryImpl(private val client: SupabaseClient) : ProfileRepository {
    override fun getUserId(): String? {
        return client.auth.currentUserOrNull()?.id
    }

    override suspend fun getProfileById(id: String): Player {
//        return client.auth.currentUserOrNull()?.let {
        return client.postgrest.from(PROFILES).select {
            filter {
                PlayerResponse::id eq id
            }
        }.decodeSingle<PlayerResponse?>()?.toPlayer() ?: throw Exception("User Not Found")
//        }
    }

    override suspend fun getMyProfile(): Player? {
        return client.auth.currentUserOrNull()?.let {
            return client.postgrest.from(PROFILES).select {
                filter {
                    PlayerResponse::id eq it.id
                }
            }.decodeSingle<PlayerResponse>().toPlayer()
        }
    }

    override suspend fun getProfileByHandle(handle: String): Player {
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

    override suspend fun loadMatches(userId: String): List<Match> {
        return client.postgrest.from(MATCHES).select {
            filter {
                RemoteMatch::host eq userId
            }
            order("created_at", Order.DESCENDING)
        }
            .decodeList<RemoteMatch>()
            .map { it.toMatch() }
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
                PlayerResponse::avatar_version setTo profile.avatarVersion
                PlayerResponse::cover setTo profile.cover
                PlayerResponse::cover_v setTo profile.coverVersion
                PlayerResponse::visibility setTo profile.visibility
                PlayerResponse::location setTo profile.location
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

    override suspend fun updateCover(avatar: ByteArray, currentV: Int) {
        val bucket = client.storage.from("avatar")
        val path = "${client.auth.currentUserOrNull()?.id}/cover.jpg"
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
                    PlayerResponse::cover setTo imageUrl
                    PlayerResponse::cover_v setTo currentV + 1
                }
            ) {
                filter {
                    PlayerResponse::id eq it.id
                }
            }
        }
    }
    override suspend fun logout() {
        client.auth.signOut()
    }


}