package proglife.com.ua.intellektiks.ui.notifications.show

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
class NotificationShowPresenter(item: NotificationMessagePreview) : BasePresenter<NotificationShowView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    init {
        injector().inject(this)
        viewState.showItem(item)
        mCommonInteractor.loadNotification(item.id)
                .compose(oAsync())
                .doOnSubscribe { viewState.showLoading() }
                .doOnNext { viewState.dismissLoading() }
                .subscribe(
                        {
                            viewState.showContent(item, it)
                        },
                        {
                            viewState.showError(R.string.error_network)
                            it.printStackTrace()
                        },
                        {}
                )
    }

}