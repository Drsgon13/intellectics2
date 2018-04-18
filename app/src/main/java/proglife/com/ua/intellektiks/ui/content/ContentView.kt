package proglife.com.ua.intellektiks.ui.content

import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource
import proglife.com.ua.intellektiks.data.models.Content
import proglife.com.ua.intellektiks.data.models.ReportMessage
import proglife.com.ua.intellektiks.data.models.MediaObject
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
    fun onEmptyMediaList()
    fun showVideo(dynamicMediaSource: DynamicConcatenatingMediaSource)
    fun seekTo(index: Int, position: Long)
    fun startDownload(mediaObject: MediaObject)
    fun startCommonDownload(mediaObject: MediaObject)
    fun showReports(show: Boolean, messages: List<ReportMessage>)
    fun checkContent(isAudio: Boolean)
    fun requestPlayerPosition()
}