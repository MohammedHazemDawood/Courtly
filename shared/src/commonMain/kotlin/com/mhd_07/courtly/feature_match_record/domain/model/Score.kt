package com.mhd_07.courtly.feature_match_record.domain.model

enum class Score(val display: String) {
    Zero("0"),
    Fifteen("15"),
    Thirty("30"),
    Forty("40"),
    Advantage("Advantage"),
    Win("Win")
}

fun Score.next(): Score {
    return Score.entries.getOrElse(ordinal + 1) { this }
}

fun Score.prev(): Score {
    return Score.entries.getOrElse(ordinal - 1) { this }
}