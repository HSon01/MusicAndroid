package com.ssn.sxmusic.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.ssn.sxmusic.util.Const
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun createNotification(app: Application): NotificationManager {
        return app.applicationContext.getSystemService(NotificationManager::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @Singleton
    fun provideNotificationChannel(notificationManager: NotificationManager): NotificationChannel {
        val notificationChannel = NotificationChannel(
            Const.ID_APPLICATION,
            Const.NAME_APPLICATION,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationChannel.setSound(null, null)
        notificationManager.createNotificationChannel(notificationChannel)
        return notificationChannel
    }

}