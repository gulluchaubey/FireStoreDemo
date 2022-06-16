package com.example.firestoredemo.details.video

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerReadyListener
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerStateListener
import com.ct7ct7ct7.androidvimeoplayer.model.TextTrack
import com.example.firestoredemo.databinding.ActivityWatchVideoBinding


class WatchVideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWatchVideoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchVideoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        lifecycle.addObserver(binding.videoView)

        binding.videoView.initialize(true, 417726914, "https://vimeo.com/")
        binding.videoView.loadVideo(417726914)


        binding.videoView.addReadyListener(object : VimeoPlayerReadyListener {
            override fun onReady(title: String?, duration: Float, textTrackArray: Array<TextTrack>) {
                binding.videoView.play()
            }

            override fun onInitFailed() {

            }
        })
    }


}