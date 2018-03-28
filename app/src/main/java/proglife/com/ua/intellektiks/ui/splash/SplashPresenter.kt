package proglife.com.ua.intellektiks.ui.splash

import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.business.CommonInteractor
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

    fun init() {
        mCommonInteractor.isAuthenticated()
                .delay(DELAY, TimeUnit.MILLISECONDS)
                .compose(sAsync())
                .subscribe(
                        {
                            if (it) viewState.showMain() else viewState.showAuth()
                        },
                        {

                        }
                )
    }

}