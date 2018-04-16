package proglife.com.ua.intellektiks.ui.lessons.show

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.FileType
import proglife.com.ua.intellektiks.data.models.Lesson
import proglife.com.ua.intellektiks.data.models.LessonPreview
import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.extensions.DownloadableFile
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import proglife.com.ua.intellektiks.ui.base.media.MediaStateHelper
import proglife.com.ua.intellektiks.utils.ExoUtils
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class LessonPresenter(private val lessonPreview: LessonPreview): BasePresenter<LessonView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    private var list = arrayListOf<FileType>()

    private var mLesson: Lesson? = null
    private val dynamicMediaSource: DynamicConcatenatingMediaSource = DynamicConcatenatingMediaSource()
    private var dispose: Disposable? = null

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
        viewState.showInfo(lessonPreview)
        mCommonInteractor.getLesson(lessonPreview.id)
                .flatMap { mCommonInteractor.existsFiles(it) }
                .compose(oAsync())
                .doOnSubscribe { viewState.showLoading() }
                .doOnNext { viewState.dismissLoading() }
                .subscribe(
                        {
                            mLesson = it
                            val mList = it.getMediaObjects()
                            mMediaStateHelper.mediaObjects = mList
                            viewState.showLesson(it, mList)
                            mList.firstOrNull()?.let { viewState.selectItem(it) }
                        },
                        {
                            if (it is IOException && mLesson == null) {
                                viewState.showNoData()
                            }
                            it.printStackTrace()
                        }
                )
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
        val mediaObjects = mLesson!!.getMediaObjects()
        val media: MutableList<MediaSource> = arrayListOf()
        val dataSourceFactory = ExoUtils.buildDataSourceFactory(context, userAgent)
        for (i in 0 until mediaObjects.size) {
            @Suppress("SENSELESS_COMPARISON")
            if (mediaObjects[i].type == MediaObject.Type.PLAYER && (mediaObjects[i].fileType == FileType.MP3 || mediaObjects[i].fileType == FileType.MP4 || mediaObjects[i].fileType == FileType.HLS)) {
                list.add(mediaObjects[i].fileType!!)
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
            viewState.emptyList()
        else {
            dynamicMediaSource.addMediaSources(media)
            viewState.showVideo(dynamicMediaSource)
        }
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
        val mediaObjects = mLesson!!.getMediaObjects()
        if(mediaObjects[index].downloadable &&
                File("${context.filesDir}/c_${mediaObjects[index].downloadableFile!!.name}").exists()){
            val dataSourceFactory = ExoUtils.buildDataSourceFactory(context, userAgent)
            val mediaSource = ExoUtils.buildMediaSource(
                    dataSourceFactory,
                    Uri.parse(File("${context.filesDir}/c_${mediaObjects[index].downloadableFile!!.name}").absolutePath),
                    mediaObjects[index].fileType!!)
            dynamicMediaSource.addMediaSource(index, mediaSource)
            dynamicMediaSource.removeMediaSource(index + 1, {
                if(currentWindowIndex == index)
                    viewState.seekTo(currentWindowIndex)
            })
        }
    }

    fun checkType(index: Int) {
        viewState.checkContent(list[index] == FileType.MP3)
    }

    fun play(mediaObject: MediaObject) {
        mLesson?.let {
            viewState.selectItem(mediaObject)
            viewState.seekTo(mMediaStateHelper.mediaObjects!!.indexOf(mediaObject))
        }
    }

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

    fun reminder(index: Int, seconds: Long){
        val mediaObject = mMediaStateHelper.mediaObjects!![index]
        mCommonInteractor.createReminder(mLesson!!.idContact,null, mLesson!!.id, seconds, mediaObject.id)
                .compose(oAsync())
                .subscribe(
                        {},
                        {
                            it.printStackTrace()
                        }
                )
    }

    fun startReminder() {
        dispose = Observable.interval(5, TimeUnit.SECONDS)
                .compose(oAsync())
                .subscribe(
                        {
                            viewState.positionLesson()
                        },
                        {
                            it.printStackTrace()
                        }
                )
    }

    fun clearTimer(){
        dispose?.dispose()
    }

}