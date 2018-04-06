package proglife.com.ua.intellektiks.ui.goods

import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import proglife.com.ua.intellektiks.data.models.Goods
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface GoodsShowView: BaseView {
    fun showInfo(item: GoodsPreview)
    fun showLoading()
    fun dismissLoading()
    fun showGoods(item: Goods, mList: List<MediaObject>)
    fun showVideo(mediaSource: ConcatenatingMediaSource)
    fun showProgress(count: Int, total: Int, progress: Int?)
    fun emptyList()
    fun showNoData()
    fun checkContent(isAudio: Boolean)
    fun seekTo(position: Int)
    fun notifyItemChanged(index: Int)
    fun startDownload(mediaObject: MediaObject)
    fun notifyDataSetChanged()
    fun startCommonDownload(mediaObject: MediaObject)
    fun selectItem(mediaObject: MediaObject)
}