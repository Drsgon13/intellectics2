package proglife.com.ua.intellektiks.ui.bonus

import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.ui.base.BasePresenter

@InjectViewState
class BonusPresenter: BasePresenter<BonusView>(){
    fun init() {
        viewState.initPlayer(BONUS_URL)
    }

    val BONUS_URL = "https://lk.sheremetev.info/private/Android_bonus/Sheremetev-Smartphone_sucess.mp3"

}