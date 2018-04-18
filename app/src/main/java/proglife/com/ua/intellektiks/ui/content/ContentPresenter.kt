package proglife.com.ua.intellektiks.ui.content

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.*
import proglife.com.ua.intellektiks.data.network.models.ReminderResponse
import proglife.com.ua.intellektiks.extensions.DownloadableFile
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import proglife.com.ua.intellektiks.ui.content.media.MediaStateHelper
import proglife.com.ua.intellektiks.utils.ExoUtils
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 18.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class ContentPresenter(goodsPreview: GoodsPreview?, lessonPreview: LessonPreview?) : BasePresenter<ContentView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    private var mContent: Content? = null

    //--------------------------------------------------------------------------
    // REMINDER
    //--------------------------------------------------------------------------
    private var mReminderDispose: Disposable? = null

    //--------------------------------------------------------------------------
    // PLAYER
    //--------------------------------------------------------------------------
    private val mDynamicMediaSource: DynamicConcatenatingMediaSource = DynamicConcatenatingMediaSource()
    private var mFileTypes = arrayListOf<FileType>()
    private var mErrorPlayPosition: Int? = null

    init {
        injector().inject(this)
        viewState.showPreview(goodsPreview ?: lessonPreview)
        if (goodsPreview != null) loadGoods(goodsPreview)
        else if (lessonPreview != null) loadLesson(lessonPreview)
    }

    private fun loadGoods(goodsPreview: GoodsPreview) {
        mCommonInteractor.getGoods(goodsPreview.id)
                .flatMap { mCommonInteractor.existsFiles(it) }
                .compose(oAsync())
                .doOnSubscribe { viewState.showLoading() }
                .doOnNext { viewState.dismissLoading() }
                .subscribe(
                        { processSuccess(it) },
                        { processError(it) }
                )
    }

    private fun loadLesson(lessonPreview: LessonPreview) {
        mCommonInteractor.getLesson(lessonPreview.id)
                .flatMap { mCommonInteractor.existsFiles(it) }
                .compose(oAsync())
                .doOnSubscribe { viewState.showLoading() }
                .doOnNext { viewState.dismissLoading() }
                .subscribe(
                        {
                            processSuccess(it)
                            if (lessonPreview.requestReport == 1) {
                                viewState.showReports(true, it.messages)
                            }
                        },
                        { processError(it) }
                )
    }

    private fun processSuccess(content: Content) {
        mContent = content
        val mList = content.getMediaObjects()
        mMediaStateHelper.mediaObjects = mList
        viewState.showContent(content, mList)
        mList.firstOrNull()?.let { viewState.selectItem(it) }
    }

    private fun processError(throwable: Throwable) {
        if (throwable is IOException && mContent == null) {
            viewState.showNoData()
        }
        throwable.printStackTrace()
    }

    //--------------------------------------------------------------------------
    // REMINDER
    //--------------------------------------------------------------------------

    fun startReminder() {
        mReminderDispose = Observable.interval(5, TimeUnit.SECONDS)
                .compose(oAsync())
                .subscribe(
                        {
                            viewState.requestPlayerPosition()
                        },
                        {
                            it.printStackTrace()
                        }
                )
    }

    fun clearTimer(){
        mReminderDispose?.dispose()
    }

    fun addReminderMark(currentWindowIndex: Int, currentPosition: Long) {
        val mediaObject = mMediaStateHelper.mediaObjects!![currentWindowIndex]
        createReminder(mContent!!, mediaObject.id, currentPosition)
                .compose(oAsync())
                .subscribe(
                        {},
                        {
                            it.printStackTrace()
                        }
                )
    }

    private fun createReminder(content: Content, mediaObjectId: Long, currentPosition: Long): Observable<ReminderResponse> {
        return if (content is Lesson) {
            mCommonInteractor.createReminder(content.contactId, null, content.id, currentPosition, mediaObjectId)
        } else {
            mCommonInteractor.createReminder(content.contactId, content.id, null, currentPosition, mediaObjectId)
        }
    }

    //--------------------------------------------------------------------------
    // DOWNLOADER
    //--------------------------------------------------------------------------

    // Запрашиваем скачивание
    fun download(mediaObject: MediaObject) {
        if (mediaObject.type == MediaObject.Type.PLAYER) {
            viewState.startDownload(mediaObject)
        } else {
            viewState.startCommonDownload(mediaObject)
        }
    }

    fun onServiceCallback(code: Int, data: Intent?) {
        mMediaStateHelper.onServiceCallback(code, data)
    }

    fun downloadAll() {
        val list = mMediaStateHelper.mediaObjects
                ?.filter { it.type == MediaObject.Type.PLAYER &&
                        (it.downloadableFile?.state == null ||
                                it.downloadableFile?.state == DownloadableFile.State.NONE) }
        if (list != null && list.isNotEmpty()) {
            list.forEach { viewState.startDownload(it) }
        }
    }

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

    //--------------------------------------------------------------------------
    // PLAYER
    //--------------------------------------------------------------------------

    fun play(mediaObject: MediaObject, position: Long) {
        mContent?.let {
            viewState.selectItem(mediaObject)
            viewState.seekTo(mMediaStateHelper.mediaObjects!!.indexOf(mediaObject), position)
        }
    }

    fun initDataSource(context: Context) {
        mCommonInteractor.getUserAgent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            initDataSource(context, it)
                        },
                        {
                            initDataSource(context, null)
                        }
                )

    }

    private fun initDataSource(context: Context, userAgent: String?) {
        val mediaObjects = mMediaStateHelper.mediaObjects!!
        val media: MutableList<MediaSource> = arrayListOf()
        val dataSourceFactory = ExoUtils.buildDataSourceFactory(context, userAgent)
        for (i in 0 until mediaObjects.size) {
            @Suppress("SENSELESS_COMPARISON")
            if (mediaObjects[i].type == MediaObject.Type.PLAYER &&(mediaObjects[i].fileType == FileType.MP3 || mediaObjects[i].fileType == FileType.MP4 || mediaObjects[i].fileType == FileType.HLS)) {
                mFileTypes.add(mediaObjects[i].fileType!!)
                val file: String = if(mediaObjects[i].downloadable &&
                        File("${context.filesDir}/c_${mediaObjects[i].downloadableFile!!.name}").exists() &&
                        mediaObjects[i].fileType != FileType.HLS)
                    File("${context.filesDir}/c_${mediaObjects[i].downloadableFile!!.name}").absolutePath else mediaObjects[i].url
                media.add(ExoUtils.buildMediaSource(
                        dataSourceFactory,
                        Uri.parse(file),
                        mediaObjects[i].fileType!!))
            }
        }

        if (media.isEmpty())
            viewState.onEmptyMediaList()
        else {
            mDynamicMediaSource.addMediaSources(media)
            viewState.showVideo(mDynamicMediaSource)
        }
    }

    fun playNextOrPrev(currentWindowIndex: Int) {
        viewState.checkContent(mFileTypes[currentWindowIndex] == FileType.MP3)
        val selectedMediaObject = mMediaStateHelper.mediaObjects?.get(currentWindowIndex)
        selectedMediaObject?.let { viewState.selectItem(it) }
    }

    fun checkSource(currentWindowIndex: Int, context: Context) {
        val mediaObject = mMediaStateHelper.mediaObjects!![currentWindowIndex]
        if(mErrorPlayPosition != currentWindowIndex && mediaObject.downloadable &&
                File("${context.filesDir}/c_${mediaObject.downloadableFile!!.name}").exists()) {
            initDataSource(context)
            play(mediaObject, 0)
        }
    }

    fun setErrorPlayPosition(currentWindowIndex: Int) {
        this.mErrorPlayPosition = currentWindowIndex
    }

    fun checkDownload(context: Context, index: Int, currentWindowIndex: Int) {
        mCommonInteractor.getUserAgent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            checkDownload(context, index, currentWindowIndex, it)
                        },
                        {
                            checkDownload(context, index, currentWindowIndex, null)
                        }
                )
    }

    private fun checkDownload(context: Context, index: Int, currentWindowIndex: Int, userAgent: String?) {
        val mediaObjects = mMediaStateHelper.mediaObjects!!
        if(mediaObjects[index].downloadable &&
                File("${context.filesDir}/c_${mediaObjects[index].downloadableFile!!.name}").exists()){
            val dataSourceFactory = ExoUtils.buildDataSourceFactory(context, userAgent)
            val mediaSource = ExoUtils.buildMediaSource(
                    dataSourceFactory,
                    Uri.parse(File("${context.filesDir}/c_${mediaObjects[index].downloadableFile!!.name}").absolutePath),
                    mediaObjects[index].fileType!!)
            mDynamicMediaSource.addMediaSource(index, mediaSource)
            mDynamicMediaSource.removeMediaSource(index + 1, {
                if(currentWindowIndex == index)
                    viewState.seekTo(currentWindowIndex, 0)
            })
        }
    }

}