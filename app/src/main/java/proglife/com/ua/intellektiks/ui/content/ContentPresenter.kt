package proglife.com.ua.intellektiks.ui.content

import android.app.Activity.RESULT_OK
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
import io.reactivex.subjects.PublishSubject
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.*
import proglife.com.ua.intellektiks.data.network.ServerException
import proglife.com.ua.intellektiks.data.network.models.ReminderResponse
import proglife.com.ua.intellektiks.data.network.models.SetFavoritesRequest
import proglife.com.ua.intellektiks.extensions.DownloadableFile
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import proglife.com.ua.intellektiks.ui.content.media.MediaStateHelper
import proglife.com.ua.intellektiks.utils.ExoUtils
import java.io.File
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 18.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class ContentPresenter(private val goodsPreview: GoodsPreview?, lessonPreview: LessonPreview?) : BasePresenter<ContentView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    private var mContent: Content? = null

    //--------------------------------------------------------------------------
    // REMINDER
    //--------------------------------------------------------------------------
    private var mReminderDispose: Disposable? = null

    //--------------------------------------------------------------------------
    // REPORTS
    //--------------------------------------------------------------------------
    private var mTypedReportEmitter = PublishSubject.create<String>()

    //--------------------------------------------------------------------------
    // PLAYER
    //--------------------------------------------------------------------------
    private var mDynamicMediaSource: DynamicConcatenatingMediaSource = DynamicConcatenatingMediaSource()
    private var mFileTypes = arrayListOf<FileType>()
    private var mErrorPlayPosition: Int? = null

    init {
        injector().inject(this)
        viewState.showPreview(goodsPreview ?: lessonPreview)
        if (goodsPreview != null) loadGoods(goodsPreview)
        else if (lessonPreview != null) loadLesson(lessonPreview)

        mTypedReportEmitter.debounce(500, TimeUnit.MILLISECONDS)
                .flatMap { mCommonInteractor.setDraft(mContent!!.id, it).toObservable() }
                .subscribe()
    }

    private fun loadGoods(goodsPreview: GoodsPreview) {
        viewState.favoriteState(!(goodsPreview.idFavorite!!.isBlank() || goodsPreview.idFavorite!! == "0"))
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
                            if (lessonPreview.requestReport == 1) prepareReports(it)
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
        viewState.dismissLoading()
    }

    //--------------------------------------------------------------------------
    // REMINDER
    //--------------------------------------------------------------------------

    fun showName(currentWindowIndex: Int){
        val mediaObject = mMediaStateHelper.mediaObjects!![currentWindowIndex]
        viewState.showNameNotification(mediaObject.title)
    }

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

    fun deleteMarker(marker: Marker){
        deleteReminder(mContent!!, marker)
                .compose(oAsync())
                .subscribe(
                        {

                        },
                        {
                            it.printStackTrace()
                        }
                )
    }

    private fun deleteReminder(content: Content, marker: Marker): Observable<Unit> {
        return if (content is Lesson) {
            mCommonInteractor.deleteReminder(content.contactId, marker.mediaobjectid,null, content.id)
        } else {
            mCommonInteractor.deleteReminder(content.contactId, marker.mediaobjectid, content.id, null)
        }
    }

    private fun createReminder(content: Content, mediaObjectId: Long, currentPosition: Long): Observable<ReminderResponse> {
        return if (content is Lesson) {
            mCommonInteractor.createReminder(content.contactId, null, content.id, currentPosition, mediaObjectId)
        } else {
            mCommonInteractor.createReminder(content.contactId, content.id, null, currentPosition, mediaObjectId)
        }
    }

    fun favorite(boolean: Boolean){
        var goods :String? = null
        var bookmark :String? = null

        if(boolean)
            goods = goodsPreview!!.id.toString()
        else bookmark = goodsPreview!!.idFavorite
        if(bookmark == "0" && !boolean) return
        mCommonInteractor.changeFavorite(if(boolean) SetFavoritesRequest.ADD else SetFavoritesRequest.DELETE, goods, bookmark)
                .compose(oAsync())
                .subscribe(
                        {
                            if(boolean)
                                goodsPreview.idFavorite = it.id.toString()
                            else goodsPreview.idFavorite = "0"
                            viewState.favoriteState(boolean)

                        },
                        {
                            if(it is ServerException && it.message!=null)
                                viewState.showError(it.message!!)
                            if(it is UnknownHostException)
                                viewState.showError(R.string.error_network)

                            viewState.favoriteState(!boolean)
                            it.printStackTrace()
                        }
                )
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


    fun playMarker(marker: Marker){
        val mediaObjects = mMediaStateHelper.mediaObjects!!
        for (item in mediaObjects) {
            if (item.id == marker.mediaobjectid &&  (item.fileType == FileType.MP3 ||item.fileType == FileType.MP4 ||item.fileType == FileType.HLS)) {
                play(item, marker.position)
            } else   deleteMarker(marker)
        }
    }

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
            if(mDynamicMediaSource.size == media.size){
                mDynamicMediaSource.releaseSource()
                mDynamicMediaSource = DynamicConcatenatingMediaSource()
            }

            mDynamicMediaSource.addMediaSources(media)
            viewState.showVideo(mDynamicMediaSource)
        }
    }

    fun playNextOrPrev(currentWindowIndex: Int) {
        if(mMediaStateHelper.mediaObjects!=null
                && currentWindowIndex >= mMediaStateHelper.mediaObjects!!.filter { it.fileType == FileType.MP3 || it.fileType == FileType.MP4|| it.fileType == FileType.HLS }.size) return
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
        val mediaObject = mediaObjects[index]
        if(mediaObject.downloadable && mediaObject.type == MediaObject.Type.PLAYER &&
                File("${context.filesDir}/c_${mediaObject.downloadableFile!!.name}").exists()){
            val dataSourceFactory = ExoUtils.buildDataSourceFactory(context, userAgent)
            val mediaSource = ExoUtils.buildMediaSource(
                    dataSourceFactory,
                    Uri.parse(File("${context.filesDir}/c_${mediaObject.downloadableFile!!.name}").absolutePath),
                    mediaObject.fileType!!)
            mDynamicMediaSource.addMediaSource(index, mediaSource)
            mDynamicMediaSource.removeMediaSource(index + 1, {
                if(currentWindowIndex == index)
                    viewState.seekTo(currentWindowIndex, 0)
            })
        }
    }

    //--------------------------------------------------------------------------
    // REPORTS
    //--------------------------------------------------------------------------

    private fun prepareReports(lesson: Lesson) {
        mCommonInteractor.getDraft(lesson.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            viewState.showReports(true, lesson.messages, it)
                        },
                        {

                        }
                )
    }

    fun onTypedReport(message: String) {
        mTypedReportEmitter.onNext(message)
    }

    fun sendReport(message: String) {
        mCommonInteractor.createLessonMessage(mContent!!.id, message)
                .flatMap {
                    if (it) mCommonInteractor.getLesson(mContent!!.id, true).singleOrError()
                    else throw IllegalArgumentException()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showLoading() }
                .doOnEvent { _, _ -> viewState.dismissLoading() }
                .subscribe(
                        {
                            (mContent as Lesson).messages = it.messages
                            viewState.showReports(true, it.messages, "")
                        },
                        {

                        }
                )
    }

    fun back() {
            viewState.result(goodsPreview)

    }


}