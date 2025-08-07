package com.nuclearcode.teyesmusicplayer.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import javax.inject.Inject

class AudioServiceConnection @Inject constructor(context: Context) : ServiceConnection {

    private var binder: AudioPlaybackService.AudioServiceBinder? = null

    private val intent = Intent(context, AudioPlaybackService::class.java)

    init {
        context.bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        binder = service as? AudioPlaybackService.AudioServiceBinder
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        binder = null
    }
    fun send(command: PlaybackCommand) {
        binder?.handleCommand(command)
    }
}