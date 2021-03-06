package proglife.com.ua.intellektiks.ui.lessons.show

import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource
import proglife.com.ua.intellektiks.data.models.Lesson
import proglife.com.ua.intellektiks.data.models.LessonPreview
import proglife.com.ua.intellektiks.data.models.Marker
import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.ui.base.BaseView
import proglife.com.ua.intellektiks.ui.content.models.ReportsViewModel

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface LessonView: BaseView {
    fun showInfo(lessonPreview: LessonPreview)
    fun showLoading()
    fun dismissLoading()
    fun showLesson(lesson: Lesson, mList: List<MediaObject>)
    fun showVideo(mediaSource: DynamicConcatenatingMediaSource)
    fun showProgress(count: Int, total: Int, progress: Int?)
    fun emptyList()
    fun showNoData()
    fun checkContent(isAudio: Boolean)
    fun seekTo(position: Int, position1: Long)
    fun notifyItemChanged(index: Int)
    fun startDownload(mediaObject: MediaObject)
    fun notifyDataSetChanged()
    fun startCommonDownload(mediaObject: MediaObject)
    fun selectItem(mediaObject: MediaObject)
    fun positionLesson()
    fun hideMarker(marker: Marker)
}