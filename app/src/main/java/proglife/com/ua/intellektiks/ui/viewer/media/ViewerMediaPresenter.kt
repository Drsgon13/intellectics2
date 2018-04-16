package proglife.com.ua.intellektiks.ui.viewer.media

import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 16.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class ViewerMediaPresenter: BasePresenter<ViewerMediaView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    init {
        injector().inject(this)
        mCommonInteractor.getUserAgent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            viewState.initPlayer(it)
                        },
                        {
                            viewState.initPlayer(null)
                        }
                )
    }

}