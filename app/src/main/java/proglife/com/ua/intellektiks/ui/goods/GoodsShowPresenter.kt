package proglife.com.ua.intellektiks.ui.goods

import android.content.Context
import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import javax.inject.Inject
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import proglife.com.ua.intellektiks.data.models.FileType
import proglife.com.ua.intellektiks.data.models.MediaObject


/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class GoodsShowPresenter(goodsPreview: GoodsPreview) : BasePresenter<GoodsShowView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    init {

        injector().inject(this)
        viewState.showInfo(goodsPreview)
        mCommonInteractor.getGoods(goodsPreview.id)
                .compose(sAsync())
                .doOnSubscribe { viewState.showLoading() }
                .doOnEvent { _, _ -> viewState.dismissLoading() }
                .subscribe(
                        {
                            viewState.showGoods(it)
                        },
                        {}
                )
    }

    fun initDataSourse(context: Context, mediaObjects: List<MediaObject>){
        val extractor = ExtractorMediaSource.Factory(buildDataSourceFactory(context))

        val media: MutableList<MediaSource> = arrayListOf()
        for (i in 0 until mediaObjects.size) {
            if(mediaObjects[i].fileType == FileType.MP4)
            media.add(extractor.createMediaSource(Uri.parse(mediaObjects[i].url)))
        }

        viewState.showVideo(ConcatenatingMediaSource(*media.toTypedArray()))
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
}