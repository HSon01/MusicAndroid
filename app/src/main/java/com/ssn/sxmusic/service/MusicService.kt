package com.ssn.sxmusic.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.ssn.sxmusic.R
import com.ssn.sxmusic.media.MediaController
import com.ssn.sxmusic.media.MediaController.setStateLive
import com.ssn.sxmusic.ui.activity.DetailSongActivity
import com.ssn.sxmusic.util.Const
import com.ssn.sxmusic.util.Const.ACTION_CLEAR
import com.ssn.sxmusic.util.Const.ACTION_NEXT
import com.ssn.sxmusic.util.Const.ACTION_PAUSE
import com.ssn.sxmusic.util.Const.ACTION_PLAYING
import com.ssn.sxmusic.util.Const.ACTION_PREVIOUS
import com.ssn.sxmusic.util.Const.FRAGMENT_SEND_DATA
import com.ssn.sxmusic.util.Const.ID_APPLICATION
import com.ssn.sxmusic.util.Const.NOTIFICATION_ID
import com.ssn.sxmusic.util.Const.REQUEST_CODE_NOTIFICATION
import com.ssn.sxmusic.util.Const.SERVICE_SEND_DATA
import com.ssn.sxmusic.util.Util




class MusicService : Service() {
    private val mediaController = MediaController
    private lateinit var context: Context
    private var musicB = MusicBroadCast()
    private var binderMusicService = MusicBinder()



    override fun onBind(intent: Intent?): IBinder {
        return binderMusicService
    }

    inner class MusicBinder : Binder() {
        fun getMusicService(): MusicService {
            return this@MusicService
        }
    }

    inner class MusicBroadCast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("TAG SERVICE", "SERVICE ${intent?.action}")
            intent?.action?.let { handleActionMusic(it) }
        }
    }

    override fun onCreate() {
        context = this
        mediaController.mediaController(context)
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_CLEAR)
        intentFilter.addAction(ACTION_NEXT)
        intentFilter.addAction(ACTION_PREVIOUS)
        intentFilter.addAction(ACTION_PAUSE)
        intentFilter.addAction(ACTION_PLAYING)
        intentFilter.addAction(FRAGMENT_SEND_DATA)
        intentFilter.addAction(SERVICE_SEND_DATA)
        registerReceiver(musicB, intentFilter)
        super.onCreate()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY // Không giữ lại intent
    }

    @SuppressLint("WrongConstant", "UnspecifiedImmutableFlag")
    private fun sendNotification() {
        val remoteView = RemoteViews(packageName, R.layout.custom_notification)

        remoteView.setTextViewText(R.id.createSong, mediaController.currentSong?.creator)
        remoteView.setTextViewText(R.id.titleSong, mediaController.currentSong?.title)
        remoteView.setImageViewResource(R.id.play, R.drawable.ic_pause)
        remoteView.setImageViewResource(R.id.clear, R.drawable.ic_clear)

        if (mediaController.getIsPlay() == Const.MEDIA_PLAYING) {
            remoteView.setOnClickPendingIntent(R.id.play, pendingIntent(this, ACTION_PAUSE))
            remoteView.setImageViewResource(R.id.play,R.drawable.ic_pause)
        } else {
            remoteView.setOnClickPendingIntent(R.id.play, pendingIntent(this, ACTION_PLAYING))
            remoteView.setImageViewResource(R.id.play, R.drawable.ic_play)
        }

        remoteView.setOnClickPendingIntent(R.id.clear, pendingIntent(this, ACTION_CLEAR))
        remoteView.setOnClickPendingIntent(R.id.next, pendingIntent(this, ACTION_NEXT))
        remoteView.setOnClickPendingIntent(R.id.previous, pendingIntent(this, ACTION_PREVIOUS))



        val resultIntent = Intent(this, DetailSongActivity::class.java)
            .apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK}

        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(REQUEST_CODE_NOTIFICATION,PendingIntent.FLAG_UPDATE_CURRENT)
        }


        val noti = NotificationCompat.Builder(this, ID_APPLICATION)
            .setSmallIcon(R.drawable.logo_app_removebg)
            .setCustomContentView(remoteView)
            .setContentIntent(resultPendingIntent)
            .setSound(null)

        startForeground(NOTIFICATION_ID, noti.build())
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun pendingIntent(context: Context, action: String): PendingIntent {
        val intent = Intent(action)
        return PendingIntent.getBroadcast(
            context.applicationContext,
            REQUEST_CODE_NOTIFICATION,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    private fun playSong(new: Boolean = false) {
        mediaController.playPauseSong(new)
        sendNotification()
    }

    private fun nextSong() {
        mediaController.nextSong()
        sendNotification()
    }

    private fun previousSong() {
        mediaController.previousSong()
        sendNotification()
    }


    private fun handleActionMusic(Action: String) {
        when (Action) {
            FRAGMENT_SEND_DATA -> {
                playSong(true)
                return
            }
            ACTION_PLAYING -> {
                playSong()
                return
            }
            ACTION_PAUSE -> {
                playSong()
                return
            }
            ACTION_NEXT -> {
                nextSong()
                return
            }
            ACTION_PREVIOUS -> {
                previousSong()
                return
            }
            ACTION_CLEAR -> {
                if(Util.isRunningActivity(context)){
                    stopForeground(true)
                }else{
                    stopSelf()
                    setStateLive(Const.MEDIA_PAUSED)
                }
                return
            }
            SERVICE_SEND_DATA -> {
                sendNotification()
                return
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(musicB)
        val mediaPlayer = mediaController.mediaPlayer
        mediaPlayer?.release()
        mediaController.mediaPlayer = null
    }


}