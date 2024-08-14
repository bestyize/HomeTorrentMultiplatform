package com.thewind.kmmplayer.platform

import android.content.Context
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PlayerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mMediaPlayer: MediaPlayer = MediaPlayer()

    private var mSurface: Surface? = null

    private val state: MutableStateFlow<PlayerState> = MutableStateFlow(PlayerState())

    private val playerScope = CoroutineScope(Dispatchers.Main)

    private val textureView: TextureView = TextureView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }


    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        addView(textureView)

        textureView.surfaceTextureListener = object : SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                mSurface = Surface(surface)
                mMediaPlayer.setSurface(mSurface)
                state.value = state.value.copy(isSurfaceAvailable = true)
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                mSurface?.release()
                mSurface = null
                mMediaPlayer.release()
                state.value = state.value.copy(isSurfaceAvailable = false)
                return true
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

            }
        }
    }

    fun play(url: String) {
        playerScope.launch {
            state.collectLatest { state ->
                if (state.isSurfaceAvailable) {
                    mMediaPlayer.setDataSource(url)
                    mMediaPlayer.prepareAsync()
                    mMediaPlayer.setOnPreparedListener {
                        mMediaPlayer.start()
                    }
                }

            }
        }
    }

    fun pause() {
        mMediaPlayer.pause()
    }

    fun stop() {
        mMediaPlayer.stop()
    }


}