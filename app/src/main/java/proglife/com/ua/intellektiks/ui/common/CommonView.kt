package proglife.com.ua.intellektiks.ui.common

import proglife.com.ua.intellektiks.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface CommonView: BaseView {
    fun showProfile()
    fun showAuth(clearStack: Boolean)
    fun showLoading()
    fun dismissLoading()
    fun showNotificationCount(notificationCount: Int)
}