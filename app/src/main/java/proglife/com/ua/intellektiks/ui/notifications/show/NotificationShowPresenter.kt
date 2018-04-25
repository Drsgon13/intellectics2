package proglife.com.ua.intellektiks.ui.notifications.show

import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.NotificationMessagePreview
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import java.util.*
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 07.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class NotificationShowPresenter(item: NotificationMessagePreview?, idMessage: String?, type: String?) : BasePresenter<NotificationShowView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    init {
        injector().inject(this)
        //viewState.showItem(item)
        when(type) {
            "1" -> loadNotificationURL(idMessage!!)
            "2" -> loadNotification(NotificationMessagePreview(
                    idMessage!!.toLong(),
                    "",
                    "",
                    Date(),
                    Date(),
                    ""

            ))
            else -> loadNotification(item!!)
        }
    }

    private fun loadNotificationURL(id: String){
        mCommonInteractor.getNotificationUrl(id.toLong())
                .compose(oAsync())
                .doOnSubscribe { viewState.showLoading() }
                .doOnNext { viewState.dismissLoading() }
                .subscribe(
                        {
                            viewState.showURL(it.notification.url)
                        },
                        {
                            viewState.showError(R.string.error_network)
                            it.printStackTrace()
                        },
                        {}
                )
    }

    private fun loadNotification(item: NotificationMessagePreview){
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