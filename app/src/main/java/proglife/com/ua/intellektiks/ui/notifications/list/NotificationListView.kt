package proglife.com.ua.intellektiks.ui.notifications.list

import proglife.com.ua.intellektiks.data.models.NotificationMessagePreview
import proglife.com.ua.intellektiks.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 07.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface NotificationListView: BaseView {
    fun showLoading()
    fun dismissLoading()
    fun showError(res: Int)
    fun showNotifications(list: List<NotificationMessagePreview>)
    fun showNotification(item: NotificationMessagePreview)
    fun showNeedAuth()
}