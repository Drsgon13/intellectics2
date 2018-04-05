package proglife.com.ua.intellektiks.ui.viewer

import android.net.Uri
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_viwer_media.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.data.models.FileType
import proglife.com.ua.intellektiks.ui.base.BaseActivity
import proglife.com.ua.intellektiks.utils.ExoUtils

class ViewerMediaActivity: BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viwer_media)
        initPlayer()
    }

    fun checkContent(isAudio: Boolean) {
        exoPlayer.controllerShowTimeoutMs = if(isAudio) Int.MAX_VALUE else 0
        exoPlayer.controllerHideOnTouch = !isAudio
    }

    private fun initPlayer(){
        val url = intent.getStringExtra(Constants.Field.CONTENT)
        val fileType = intent.getSerializableExtra(Constants.Field.TYPE) as FileType
        val dataSourceFactory = ExoUtils.buildDataSourceFactory(this)
        val mediaSource = ExoUtils.buildMediaSource(
                dataSourceFactory,
                Uri.parse(url),
                fileType)

        val player = ExoUtils.initExoPlayerFactory(this)
        exoPlayer!!.player = player
        player.prepare(mediaSource)

        checkContent(fileType == FileType.MP3)
    }

    override fun onPause() {
        super.onPause()
        if(exoPlayer.player!=null)
            exoPlayer.player.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if(exoPlayer.player!=null)
            exoPlayer.player.release()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        withBackAnimation()
    }
}