package com.github.iyboklee.model

import java.time.LocalDate

data class Artist(
    val seq: Long,
    val name: String,
    val members: Int,
    val debutAt: LocalDate
)

data class Album(
    val seq: Long,
    val title: String,
    val genre: String?,
    val issueAt: LocalDate
)

data class Song(
    val seq: Long,
    val title: String
)