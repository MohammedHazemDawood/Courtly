package com.mhd_07.courtly.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Score(val display: String) {
    Zero("0"),
    Fifteen("15"),
    Thirty("30"),
    Forty("40"),
    Advantage("AD"),
    Win("Win")
}

fun Score.next(): Score {
    return Score.entries.let {
        it.getOrElse(ordinal + 1) { this }
    }
}

fun Score.prev(): Score {
    return Score.entries.getOrElse(ordinal - 1) { this }
}