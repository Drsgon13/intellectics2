package proglife.com.ua.intellektiks.ui.notifications.list

import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.NotificationMessagePreview
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 07.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class NotificationListPresenter: BasePresenter<NotificationListView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    init {
        injector().inject(this)
        mCommonInteractor.loadNotifications()
                .compose(oAsync())
                .doOnSubscribe { viewState.showLoading() }
                .doOnNext { viewState.dismissLoading() }
                .subscribe(
                        {
                            viewState.showNotifications(it)
                        },
                        {
                            viewState.showError(R.string.error_network)
                            it.printStackTrace()
                        },
                        {}
                )
    }

    fun openNotification(item: NotificationMessagePreview) {
        viewState.showNotification(item)
    }

}