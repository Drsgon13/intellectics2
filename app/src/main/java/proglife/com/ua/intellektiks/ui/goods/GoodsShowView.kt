package proglife.com.ua.intellektiks.ui.goods

import proglife.com.ua.intellektiks.data.models.Goods
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface GoodsShowView: BaseView {
    fun showInfo(item: GoodsPreview)
    fun showLoading()
    fun dismissLoading()
    fun showGoods(item: Goods)
    fun showProgress(count: Int, total: Int, progress: Int?)
}