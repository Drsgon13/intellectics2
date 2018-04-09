package proglife.com.ua.intellektiks.ui.settings

import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class SettingsPresenter: BasePresenter<SettingsView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    init {
        injector().inject(this)
        mCommonInteractor.userData()
                .compose(oAsync())
                .subscribe(
                        {
                            viewState.showData(it)
                        },
                        {

                        }
                )
        mCommonInteractor.getMediaCacheSize()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            viewState.showCacheSize(it)
                        },
                        {}
                )
    }

    fun clearCache() {
        mCommonInteractor.clearMediaCache()
                .flatMap { mCommonInteractor.getMediaCacheSize() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showClearCacheLoading() }
                .doOnEvent { _, _ -> viewState.dismissClearCacheLoading() }
                .subscribe(
                        {
                            viewState.showCacheSize(it)
                        },
                        {

                        }
                )
    }

}