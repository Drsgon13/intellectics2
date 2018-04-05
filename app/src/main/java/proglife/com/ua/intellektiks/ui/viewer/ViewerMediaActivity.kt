package proglife.com.ua.intellektiks.ui.viewer

import android.content.Context
import android.net.Uri
import android.os.Bundle
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_viwer_media.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.data.models.FileType
import proglife.com.ua.intellektiks.ui.base.BaseActivity

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
        val dataSourceFactory = buildDataSourceFactory(this)
        val mediaSourсe = buildMediaSource(
                dataSourceFactory,
                Uri.parse(url),
                fileType)

        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        val loadControl = DefaultLoadControl()
        val player = ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(this), trackSelector, loadControl)
        exoPlayer!!.player = player
        player.prepare(mediaSourсe)

        checkContent(fileType == FileType.MP3)
    }

    private fun buildMediaSource(
            factory: DataSource.Factory,
            uri: Uri,
            fileType: FileType): MediaSource {
        return when (fileType) {
            FileType.HLS -> HlsMediaSource.Factory(factory)
                    .createMediaSource(uri)
            else -> ExtractorMediaSource.Factory(factory)
                    .createMediaSource(uri)
        }
    }

    // Build Data Source Factory using DefaultBandwidthMeter and HttpDataSource.Factory
    private fun buildDataSourceFactory(context: Context): DefaultDataSourceFactory {
        val userAgent = Util.getUserAgent(context, "android")
        val bandwidthMeter = DefaultBandwidthMeter()
        return DefaultDataSourceFactory(context, bandwidthMeter, buildHttpDataSourceFactory(userAgent, bandwidthMeter))
    }

    // Build Http Data Source Factory using DefaultBandwidthMeter
    private fun buildHttpDataSourceFactory(userAgent: String, bandwidthMeter: DefaultBandwidthMeter): HttpDataSource.Factory {
        return DefaultHttpDataSourceFactory(userAgent, bandwidthMeter)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        withBackAnimation()
    }
}