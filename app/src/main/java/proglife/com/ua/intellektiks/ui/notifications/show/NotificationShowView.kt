package proglife.com.ua.intellektiks.ui.notifications.show

import proglife.com.ua.intellektiks.data.models.NotificationMessage
import proglife.com.ua.intellektiks.data.models.NotificationMessagePreview
import proglife.com.ua.intellektiks.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 07.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface NotificationShowView: BaseView {
    fun showLoading()
    fun dismissLoading()
    fun showError(res: Int)
    fun showContent(it: NotificationMessagePreview, item: NotificationMessage)
    fun showItem(item: NotificationMessagePreview)
    fun showURL(url: String)
}