package proglife.com.ua.intellektiks.ui.main

import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.data.network.ServerException
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class MainPresenter: BasePresenter<MainView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    private var mGoods: List<GoodsPreview> = emptyList()

    init {
        injector().inject(this)

        mCommonInteractor.isAuthenticated()
                .subscribe(
                        {
                            if (it) loadGoods() else viewState.showNeedAuth()
                        },
                        {}
                )
    }

    private fun loadGoods() {
        mCommonInteractor.loadData()
                .compose(sAsync())
                .doOnSubscribe { viewState.showLoading() }
                .doOnEvent { _, _ -> viewState.dismissLoading() }
                .subscribe(
                        {
                            mGoods = it
                            viewState.showGoods(it)
                        },
                        {
                            when (it) {
                                is ServerException -> viewState.showError(it.message)
                            }
                        }
                )
    }

    fun profile() {

    }

    fun selectGoods(goods: GoodsPreview) {
        if (goods.trainingId == null) {
            viewState.showGoods(goods)
        } else {
            viewState.showLessons(goods)
        }
    }

}