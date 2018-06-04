package proglife.com.ua.intellektiks.ui.main

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface MainView: BaseView {
    fun showLoading()
    fun dismissLoading()
    fun showError(message: String?)
    fun showNeedAuth()
    fun showGoods(list: List<GoodsPreview>)
    fun showGoods(item: GoodsPreview)
    fun showLessons(item: GoodsPreview)
    fun showNoData()
    @StateStrategyType(SingleStateStrategy::class)
    fun showRateRequest()
}