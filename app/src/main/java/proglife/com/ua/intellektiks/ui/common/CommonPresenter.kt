package proglife.com.ua.intellektiks.ui.common

import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class CommonPresenter: BasePresenter<CommonView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    init {
        injector().inject(this)
    }

    fun profile() {
        mCommonInteractor.isAuthenticated()
                .compose(sAsync())
                .subscribe(
                        {
                            if (it) viewState.showProfile() else viewState.showAuth(false)
                        },
                        {}
                )
    }

    fun logout() {
        mCommonInteractor.logout()
                .compose(sAsync())
                .subscribe(
                        {
                            viewState.showAuth(true)
                        },
                        {}
                )
    }

}