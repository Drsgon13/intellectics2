package proglife.com.ua.intellektiks.ui.goods

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.FileType
import proglife.com.ua.intellektiks.data.models.Goods
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import proglife.com.ua.intellektiks.ui.base.media.MediaStateHelper
import proglife.com.ua.intellektiks.utils.ExoUtils
import java.io.IOException
import javax.inject.Inject


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

    private val mMediaStateHelper = MediaStateHelper(object : MediaStateHelper.Callback {
        override fun onProgressChange(current: Int, total: Int, progress: Int?) {
            viewState.showProgress(current, total, progress)
        }

        override fun onItemChange(index: Int) {
            viewState.notifyItemChanged(index)
        }

        override fun onDataChange() {
            viewState.notifyDataSetChanged()
        }
    })

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
                            val mList = it.getMediaObjects()
                            mMediaStateHelper.mediaObjects = mList
                            viewState.showGoods(it, mList)
                        },
                        {
                            if (it is IOException && mGoods == null) {
                                viewState.showNoData()
                            }
                            it.printStackTrace()
                        }
                )
    }

    fun initDataSource(context: Context, mediaObjects: List<MediaObject>){
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

    // Запрашиваем скачивание
    fun download(mediaObject: MediaObject) {
        if (mediaObject.type == MediaObject.Type.PLAYER) {
            viewState.startDownload(mediaObject)
        }
    }

    fun onServiceCallback(code: Int, data: Intent?) {
        mMediaStateHelper.onServiceCallback(code, data)
    }

}