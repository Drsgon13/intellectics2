package proglife.com.ua.intellektiks.ui.splash

import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 22.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class SplashPresenter: BasePresenter<SplashView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    companion object {
        const val DELAY = 1200L
    }

    init {
        injector().inject(this)
    }

    fun init(intent: Intent) {
        val bundle = if(intent.extras != null) intent.extras else Bundle()
        if(bundle.get(Constants.Field.TYPE) != null && bundle.get(Constants.Field.TYPE) == "1")
            loadUrl(bundle.getString(Constants.Field.ID_MESSAAGE).toLong())
        else checkAuth(bundle)
    }

    fun checkAuth(bundle: Bundle){
        mCommonInteractor.isAuthenticated()
                .delay(DELAY, TimeUnit.MILLISECONDS)
                .compose(sAsync())
                .subscribe(
                        {
                            if (it) {
                                if(bundle.get(Constants.Field.TYPE) != null && bundle.get(Constants.Field.TYPE) == "2")
                                    viewState.showNotification(bundle.getString(Constants.Field.ID_MESSAAGE))
                                else viewState.showMain()
                            } else viewState.showAuth()
                        },
                        {

                        }
                )
    }

    fun loadUrl(id: Long){
        mCommonInteractor.getNotificationUrl(id)
                .compose(oAsync())
                .subscribe(
                        {
                            viewState.openBrowser(it.notification.url)
                        },
                        {
                            checkAuth(Bundle())
                        }
                )
    }
}