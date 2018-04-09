package proglife.com.ua.intellektiks.ui.bonus

import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.ui.base.BaseView

interface BonusView: BaseView {

    fun initPlayer(url: MediaObject)
    fun showProgress(current: Int, total: Int, progress: Int?)
    fun startDownload(mediaObject: MediaObject)
}