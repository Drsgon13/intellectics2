package proglife.com.ua.intellektiks.ui.profile

import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class ProfilePresenter: BasePresenter<ProfileView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    init {
        injector().inject(this)
        mCommonInteractor.userData()
                .compose(sAsync())
                .subscribe(
                        {
                            viewState.showData(it)
                        },
                        {

                        }
                )
    }

}