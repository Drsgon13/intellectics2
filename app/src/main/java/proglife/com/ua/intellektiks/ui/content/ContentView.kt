package proglife.com.ua.intellektiks.ui.content

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource
import proglife.com.ua.intellektiks.data.models.*
import proglife.com.ua.intellektiks.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 18.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface ContentView: BaseView {
    fun showPreview(previewModel: Any?)
    fun showLoading()
    fun dismissLoading()
    fun showProgress(current: Int, total: Int, progress: Int?)
    fun notifyItemChanged(index: Int)
    fun notifyDataSetChanged()
    fun showContent(content: Content, list: List<MediaObject>)
    fun selectItem(mediaObject: MediaObject)
    fun showNoData()
    fun showError(res: Int)
    fun showError(message: String)
    fun onEmptyMediaList()
    fun showVideo(dynamicMediaSource: DynamicConcatenatingMediaSource)
    fun seekTo(index: Int, position: Long)
    fun startDownload(mediaObject: MediaObject)
    fun startCommonDownload(mediaObject: MediaObject)
    fun showReports(show: Boolean, messages: List<ReportMessage>, draft: String)
    fun checkContent(isAudio: Boolean)
    @StateStrategyType(SingleStateStrategy::class)
    fun requestPlayerPosition()

    fun showNameNotification(title: String)
    fun favoriteState(favorite: Boolean)
    fun result(goodsPreview: GoodsPreview?)
}