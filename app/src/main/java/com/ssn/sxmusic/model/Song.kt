package com.ssn.sxmusic.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "Song")
data class Song(
    val avatar: String,
    val bgImage: String,
    val coverImage: String,
    val creator: String,
    val lyric: String,
    val music: String,
    @PrimaryKey
    @ColumnInfo(name = "_name")
    val title: String,
    val url: String
) : Serializable



