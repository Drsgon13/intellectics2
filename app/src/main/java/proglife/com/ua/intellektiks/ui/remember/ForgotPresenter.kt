package proglife.com.ua.intellektiks.ui.remember

import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 04.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class ForgotPresenter: BasePresenter<ForgotView>(){

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    init {
        injector().inject(this)
    }

    fun recoveryPassword(email: String) {
        mCommonInteractor.recoveryPassword(email)
                .compose(sAsync())
                .doOnSubscribe { viewState.showLoading() }
                .doFinally { viewState.dismissLoading() }
                .subscribe(
                        {
                            viewState.showSuccess(email)
                        },
                        {
                            viewState.showError(it.message)
                        }
                )
    }

}