package com.ssn.sxmusic.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ssn.sxmusic.model.Song


@Database(entities = [Song::class], exportSchema = false, version = 1)
abstract class SongDatabase : RoomDatabase() {

    abstract fun getSongDao(): SongDao

    companion object {
        private var instance: SongDatabase? = null

        fun getStudentDatabase(context: Context): SongDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, SongDatabase::class.java, "song.db").build()
            }
        }
    }
}