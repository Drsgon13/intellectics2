package proglife.com.ua.intellektiks.utils

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import proglife.com.ua.intellektiks.data.models.FileType

class ExoUtils{

    companion object {

        fun initExoPlayerFactory(context: Context): SimpleExoPlayer {
            val bandwidthMeter = DefaultBandwidthMeter()
            val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
            val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
            val loadControl = DefaultLoadControl()
            return ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(context), trackSelector, loadControl)
        }

        fun buildMediaSource(
                factory: DataSource.Factory,
                uri: Uri,
                fileType: FileType): MediaSource {
            return when (fileType) {
                FileType.HLS -> HlsMediaSource.Factory(factory)
                        .createMediaSource(uri)
                else -> ExtractorMediaSource.Factory(factory)
                        .setExtractorsFactory(DefaultExtractorsFactory())
                        .createMediaSource(uri)
            }
        }

        fun buildDataSourceFactory(context: Context): DefaultDataSourceFactory {
            val userAgent = Util.getUserAgent(context, "android")
            val bandwidthMeter = DefaultBandwidthMeter()
            return DefaultDataSourceFactory(context, bandwidthMeter, buildHttpDataSourceFactory(userAgent, bandwidthMeter))
        }

        private fun buildHttpDataSourceFactory(userAgent: String, bandwidthMeter: DefaultBandwidthMeter): HttpDataSource.Factory {
            return DefaultHttpDataSourceFactory(userAgent, bandwidthMeter)
        }
    }
}