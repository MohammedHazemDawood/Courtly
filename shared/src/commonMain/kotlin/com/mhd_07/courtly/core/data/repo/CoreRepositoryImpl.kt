package com.mhd_07.courtly.core.data.repo

import com.mhd_07.courtly.core.data.mapper.toPlayer
import com.mhd_07.courtly.core.data.model.CheckHandle
import com.mhd_07.courtly.core.data.model.CheckHandleRequest
import com.mhd_07.courtly.core.data.model.CheckHandleResponse
import com.mhd_07.courtly.core.data.model.PROFILES
import com.mhd_07.courtly.core.data.model.PlayerResponse
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.repo.CoreRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import io.ktor.client.call.body
import io.ktor.http.ContentType

class CoreRepositoryImpl(private val client: SupabaseClient) : CoreRepository {
    override suspend fun logout() {
        client.auth.signOut()
    }

    override suspend fun getProfile(): Player? {
        return client.auth.currentUserOrNull()?.let {
            return client.postgrest.from(PROFILES).select {
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

}