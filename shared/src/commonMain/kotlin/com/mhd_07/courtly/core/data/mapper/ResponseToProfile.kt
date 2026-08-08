package com.mhd_07.courtly.core.data.mapper

import com.mhd_07.courtly.core.data.model.ProfileResponse
import com.mhd_07.courtly.core.domain.model.Profile

fun ProfileResponse.toProfile() = Profile(
    id = id,
    handle = handle,
    name = display_name,
    avatar = avatar_path
)