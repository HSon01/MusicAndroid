package com.ssn.sxmusic.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssn.sxmusic.model.Song

@Dao
interface SongDao {

    @Query("SELECT * FROM Song")
     fun getAllSong(): LiveData<List<Song>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(s: Song)

    @Query("DELETE FROM Song where _name =:name")
    suspend fun deleteSongByName(name: String)

    @Query("select * from Song where _name =:name")
    suspend fun findSongByName(name: String):Song
}