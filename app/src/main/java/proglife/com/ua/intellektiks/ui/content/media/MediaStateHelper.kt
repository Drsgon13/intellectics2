package proglife.com.ua.intellektiks.ui.content.media

import android.content.Intent
import android.os.Parcelable
import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.extensions.DownloadService
import proglife.com.ua.intellektiks.extensions.DownloadableFile

/**
 * Created by Evhenyi Shcherbyna on 05.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class MediaStateHelper(private val mCallback: Callback) {

    var mediaObjects: List<MediaObject>? = null

    // Текущее состояние процесса скачивания
    private var mDownloadableFilesTotal: Int = 0
    private var mDownloadableFilesCurrent: Int = 0
    private var mProgress: Int? = null

    // Синхронизируем перечень файлов с сервера с данными сервиса для скачивания
    private fun initState(files: List<DownloadableFile>?) {
        if (files == null) return
        println("Download service: initState")
        mediaObjects?.forEach { mo ->
            files.forEach { df ->
                if (df.id == mo.id && mo.downloadable) mo.downloadableFile = df
            }
        }
        mDownloadableFilesTotal = files.size
        mDownloadableFilesCurrent = files.filter { it.state == DownloadableFile.State.FINISHED }.size
        mProgress = files.firstOrNull { it.state == DownloadableFile.State.PROCESSING }?.progress
        mCallback.onDataChange()
        mCallback.onProgressChange(mDownloadableFilesCurrent, mDownloadableFilesTotal, mProgress)
    }

    // Обрабатываем прогресс скачивания файла
    private fun progress(file: DownloadableFile?) {
        if (file == null) return
        println("Download service: progress")
        mProgress = file.progress
        mCallback.onProgressChange(mDownloadableFilesCurrent, mDownloadableFilesTotal, file.progress)
    }

    // Изменение статуса скачивания
    private fun changeState(file: DownloadableFile?) {
        if (file == null) return
        println("Download service: changeState")
        // Следим за кол-вом файлов в уведомлении
        when (file.state) {
            DownloadableFile.State.AWAIT -> mDownloadableFilesTotal += 1
            DownloadableFile.State.FINISHED -> {
                mDownloadableFilesCurrent +=1
                mProgress = null
            }
            DownloadableFile.State.FAILED -> {
                mProgress = null
            }
            else -> {}
        }
        mCallback.onProgressChange(mDownloadableFilesCurrent, mDownloadableFilesTotal, mProgress)

        // Меняем статус скачивания в MediaObject для отображения в списке
        mediaObjects?.forEachIndexed { index, mediaObject ->
            if (mediaObject.id == file.id && mediaObject.downloadable) {
                mediaObject.downloadableFile = file
                mCallback.onItemChange(index)
            }
        }
    }

    fun onServiceCallback(code: Int, data: Intent?) {
        when (code) {
            DownloadService.CODE_INIT -> {
                val parcelables: Array<Parcelable>? = data?.getParcelableArrayExtra(DownloadService.FILES)
                val files = parcelables?.map { it as DownloadableFile }
                initState(files)
            }
            DownloadService.CODE_CHANGE_STATE -> {
                val downloadableFile = data?.getParcelableExtra<DownloadableFile>(DownloadService.DOWNLOADED_FILE)
                changeState(downloadableFile)
            }
            DownloadService.CODE_CHANGE_PROGRESS -> {
                val downloadableFile = data?.getParcelableExtra<DownloadableFile>(DownloadService.DOWNLOADED_FILE)
                progress(downloadableFile)
            }
        }
    }

    interface Callback {
        fun onProgressChange(current: Int, total: Int, progress: Int?)
        fun onItemChange(index: Int)
        fun onDataChange()
    }

}