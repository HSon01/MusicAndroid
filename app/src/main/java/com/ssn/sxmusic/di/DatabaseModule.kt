package com.ssn.sxmusic.di

import android.app.Application
import androidx.room.Room
import com.ssn.sxmusic.database.SongDao
import com.ssn.sxmusic.database.SongDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Sinh Ra 1 lan
class DatabaseModule {


    @Provides //Cung Cap
    @Singleton //Duy Nhat
    fun providesSongDB(app: Application): SongDatabase {
        return Room.databaseBuilder(
            app.applicationContext,
            SongDatabase::class.java,
            "song.db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesSongDao(db: SongDatabase):SongDao{
        return db.getSongDao()
    }
}
