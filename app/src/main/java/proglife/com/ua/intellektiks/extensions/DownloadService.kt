package proglife.com.ua.intellektiks.extensions

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import proglife.com.ua.intellektiks.data.models.MediaObject
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL


/**
 * Created by Evhenyi Shcherbyna on 30.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class DownloadService : Service() {

    companion object {
        const val FILES = "files"
        const val CODE_UPDATE_STATE = 7777
        const val PENDING_INTENT = "pending_intent"
        const val MEDIA_OBJECT_IDS = "media_object_ids"
        const val MEDIA_OBJECT = "media_object"
    }

    private var mQueue: MutableList<DownloadableFile> = mutableListOf()
    private var mIsRun: Boolean = false

    private var mActualPi: PendingIntent? = null
    private var mActualMediaObjectIds: LongArray? = null


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val mediaObject = intent?.getParcelableExtra<MediaObject>(MEDIA_OBJECT)
        if (mediaObject != null) {
            if (mQueue.all{ it.id != mediaObject.id }) {
                mQueue.add(DownloadableFile.fromMediaObject(mediaObject))
                if (!mIsRun) startDownload()
            }
        }

        val mediaObjectIds = intent?.getLongArrayExtra(MEDIA_OBJECT_IDS)
        val pi: PendingIntent? = intent?.getParcelableExtra(PENDING_INTENT)
        if (pi != null && mediaObjectIds != null) {
            mActualPi = pi
            mActualMediaObjectIds = mediaObjectIds
            notifyDataSetChanged()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startDownload() {
        val nextDownloadableFile = getNextAwait()
        if (nextDownloadableFile == null) {
            mIsRun = false
            return
        } else {
            mIsRun = true
        }
        Thread(Runnable {
            val filename = nextDownloadableFile.name
            var input: BufferedInputStream? = null
            var output: FileOutputStream? = null
            try {
                val url = URL(nextDownloadableFile.url)
                val connection = url.openConnection()
                connection.connect()
                val fileLength = connection.contentLength
                input = BufferedInputStream(connection.getInputStream())
                output = openFileOutput(filename, Context.MODE_PRIVATE)

                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var length: Int
                var loadedLength = 0

                nextDownloadableFile.state = DownloadableFile.State.PROCESSING
                while (true) {
                    length = input.read(buffer)
                    if (length <= 0) break

                    output.write(buffer, 0, length)
                    loadedLength += length

                    val progress = (loadedLength.toFloat() / fileLength * 100).toInt()

                    if (progress - nextDownloadableFile.progress >= 2) {
                        nextDownloadableFile.progress = progress
                        notifyDataSetChanged()
                    }
                }
                val inFile = File("$filesDir/$filename")
                val outFile = File("$filesDir/c_$filename")
                inFile.renameTo(outFile)
                nextDownloadableFile.state = DownloadableFile.State.FINISHED
            } catch (e: IOException) {
                e.printStackTrace()
                nextDownloadableFile.state = DownloadableFile.State.FAILED
            } finally {
                output?.flush()
                output?.close()
                input?.close()
                notifyDataSetChanged()
            }
            startDownload()
        }).start()
    }

    private fun getNextAwait(): DownloadableFile? {
        return mQueue.firstOrNull { it.state == DownloadableFile.State.AWAIT }
    }

    private fun notifyDataSetChanged() {
        if (mActualMediaObjectIds == null || mActualPi == null) return
        val list = mQueue.filter { mActualMediaObjectIds!!.contains(it.id) }.toTypedArray()
        mActualPi?.send(this, CODE_UPDATE_STATE, Intent().putExtra(FILES, list))
    }

}