package proglife.com.ua.intellektiks.ui.bonus

import android.content.Intent
import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.data.models.FileType
import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.extensions.DownloadableFile
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import proglife.com.ua.intellektiks.ui.base.media.MediaStateHelper

@InjectViewState
class BonusPresenter: BasePresenter<BonusView>(){


    lateinit var mediaObject: MediaObject
    val BONUS_URL = "https://lk.sheremetev.info/private/Android_bonus/Sheremetev-Smartphone_sucess.mp3"

    fun init() {
        mediaObject = MediaObject(0, FileType.MP3, BONUS_URL, "bonus", "", "", "", 0,  false, MediaObject.Type.PLAYER, DownloadableFile(0, BONUS_URL, "bonus"))
        viewState.initPlayer(mediaObject)
        mMediaStateHelper.mediaObjects = arrayListOf(mediaObject)
    }

    private val mMediaStateHelper = MediaStateHelper(object : MediaStateHelper.Callback {
        override fun onProgressChange(current: Int, total: Int, progress: Int?) {
            viewState.showProgress(current, total, progress)
        }

        override fun onItemChange(index: Int) {}
        override fun onDataChange() {}
    })

    fun onServiceCallback(code: Int, data: Intent?) {
        mMediaStateHelper.onServiceCallback(code, data)
    }

    fun startDownload() {
        viewState.startDownload(mediaObject)
    }
}