package proglife.com.ua.intellektiks.ui.goods

import android.content.Context
import android.net.Uri
import android.support.annotation.Nullable
import com.arellomobile.mvp.InjectViewState
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.extensions.DownloadableFile
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import javax.inject.Inject
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import proglife.com.ua.intellektiks.data.models.FileType
import proglife.com.ua.intellektiks.data.models.Goods
import proglife.com.ua.intellektiks.data.models.MediaObject
import java.io.IOException
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import android.text.TextUtils
import android.util.Log
import com.google.android.exoplayer2.source.MediaSourceEventListener
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import proglife.com.ua.intellektiks.utils.ExoUtils


/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class GoodsShowPresenter(goodsPreview: GoodsPreview) : BasePresenter<GoodsShowView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    private var list = arrayListOf<FileType>()

    private var mGoods: Goods? = null

    init {
        injector().inject(this)
        viewState.showInfo(goodsPreview)
        mCommonInteractor.getGoods(goodsPreview.id)
                .flatMap { mCommonInteractor.existsFiles(it) }
                .compose(oAsync())
                .doOnSubscribe { viewState.showLoading() }
                .doOnNext { viewState.dismissLoading() }
                .subscribe(
                        {
                            mGoods = it
                            viewState.showGoods(it)
                        },
                        {
                            if (it is IOException && mGoods == null) {
                                viewState.showNoData()
                            }
                        }
                )
    }

    fun progress(files: List<DownloadableFile>?) {
        if (files == null) return
        val total: Int = files.size
        val count: Int = files.filter { it.state == DownloadableFile.State.FINISHED }.size
        val progress: Int? = files.firstOrNull { it.state == DownloadableFile.State.PROCESSING }?.progress
        viewState.showProgress(count, total, progress)
    }

    fun initDataSourse(context: Context, mediaObjects: List<MediaObject>){
        val media: MutableList<MediaSource> = arrayListOf()
        val dataSourceFactory = ExoUtils.buildDataSourceFactory(context)
        for (i in 0 until mediaObjects.size) {
            @Suppress("SENSELESS_COMPARISON")
            if (mediaObjects[i].fileType == FileType.MP3 || mediaObjects[i].fileType == FileType.MP4 || mediaObjects[i].fileType == FileType.HLS) {
                list.add(mediaObjects[i].fileType!!)
                media.add(ExoUtils.buildMediaSource(
                        dataSourceFactory,
                        Uri.parse(mediaObjects[i].url),
                        mediaObjects[i].fileType!!))
            }
        }

        if(media.isEmpty())
            viewState.emptyList()
        else
            viewState.showVideo(ConcatenatingMediaSource(*media.toTypedArray()))
    }

    fun checkType(index: Int) {
        viewState.checkContent(list[index] == FileType.MP3)
    }

    fun play(mediaObject: MediaObject) {
        mGoods?.let { viewState.seekTo(it.playerElements.indexOf(mediaObject) )   }
    }

}