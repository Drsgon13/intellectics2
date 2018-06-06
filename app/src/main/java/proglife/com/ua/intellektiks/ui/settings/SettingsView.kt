package proglife.com.ua.intellektiks.ui.settings

import proglife.com.ua.intellektiks.data.models.Card
import proglife.com.ua.intellektiks.data.models.UserData
import proglife.com.ua.intellektiks.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface SettingsView: BaseView {
    fun showData(userData: Pair<String?, UserData?>)
    fun showCacheSize(size: Long)
    fun showClearCacheLoading()
    fun dismissClearCacheLoading()
    fun showCardsLoading()
    fun dismissCardsLoading()
    fun showCards(cards: List<Card>)
    fun showCardsError(message: String?)
    fun showError(res: Int)
    fun showRemoveCardLoading()
    fun dismissRemoveCardLoading()
}