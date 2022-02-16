package com.ssn.sxmusic.model

import java.io.Serializable


data class Song(
    val avatar: String,
    val bgImage: String,
    val coverImage: String,
    val creator: String,
    val lyric: String,
    val music: String,
    val title: String,
    val url: String
) : Serializable

data class SongsX(
    val name: String,
    val songs: List<Song>,
    val url: String,
)

data class Songs(
    val top100_AM: List<SongsX>,
    val top100_CA: List<SongsX>,
    val top100_KL: List<SongsX>,
    val top100_VN: List<SongsX>
)
