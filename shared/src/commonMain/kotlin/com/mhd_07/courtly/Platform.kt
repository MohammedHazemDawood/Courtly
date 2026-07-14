package com.mhd_07.courtly

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform