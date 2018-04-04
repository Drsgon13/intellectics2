package proglife.com.ua.intellektiks.ui.base.media

import android.content.Context
import android.content.Intent
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.data.models.FileType
import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.ui.viewer.ViewerMediaActivity
import proglife.com.ua.intellektiks.ui.viewer.ViewerPdfActivity
import proglife.com.ua.intellektiks.ui.viewer.ViewerTxtActivity

/**
 * Created by Evhenyi Shcherbyna on 04.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class MediaViewer {

    companion object {
        fun open(context: Context, mediaObject: MediaObject): Intent? {
            return when {
                mediaObject.fileType == FileType.TXT -> openTxt(context, mediaObject)
                mediaObject.fileType == FileType.PDF -> openPdf(context, mediaObject)
                isMedia(mediaObject.fileType) -> openMedia(context, mediaObject)
                else -> null
            }
        }

        private fun isMedia(fileType: FileType?): Boolean {
            return fileType == FileType.MP3 || fileType == FileType.MP4 || fileType == FileType.HLS
        }

        private fun openTxt(context: Context, mediaObject: MediaObject): Intent {
            val intent = Intent(context, ViewerTxtActivity::class.java)
            intent.putExtra(Constants.Field.TITLE, mediaObject.title)
            intent.putExtra(Constants.Field.CONTENT, mediaObject.description)
            return intent
        }

        private fun openPdf(context: Context, mediaObject: MediaObject): Intent {
            val intent = Intent(context, ViewerPdfActivity::class.java)
            intent.putExtra(Constants.Field.TITLE, mediaObject.title)
            intent.putExtra(Constants.Field.CONTENT, mediaObject.url)
            return intent
        }

        private fun openMedia(context: Context, mediaObject: MediaObject): Intent {
            val intent = Intent(context, ViewerMediaActivity::class.java)
            intent.putExtra(Constants.Field.TITLE, mediaObject.title)
            intent.putExtra(Constants.Field.CONTENT, mediaObject.url)
            intent.putExtra(Constants.Field.TYPE, mediaObject.fileType)
            return intent
        }

    }

}