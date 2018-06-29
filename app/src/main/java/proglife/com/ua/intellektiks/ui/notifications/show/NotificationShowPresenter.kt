package proglife.com.ua.intellektiks.ui.notifications.show

import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.Card
import proglife.com.ua.intellektiks.data.models.NotificationMessage
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

    var mNotificationMessage: NotificationMessage? = null

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
                    "",
                    null
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
                            updateNotification(id.toLong())
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
                            updateNotification(item.id)
                            mNotificationMessage = it
                            if (it?.offerId != null && it.offerId != 0L) viewState.changeCanOrderState(true)
                            viewState.showContent(item, it)
                        },
                        {
                            viewState.showError(R.string.error_network)
                            it.printStackTrace()
                        },
                        {}
                )
    }
    private fun updateNotification(item: Long){
        mCommonInteractor.updateNotification(item)
                .compose(oAsync())
                .subscribe(
                        {
                            viewState.updateNotif()
                        },
                        {
                            viewState.showError(R.string.error_network)
                            it.printStackTrace()
                        },
                        {}
                )
    }

    fun makeOrder(card: Card? = null) {
        if (mNotificationMessage?.offerId == null || mNotificationMessage?.offerId == 0L) return
        mCommonInteractor.callPayment(mNotificationMessage!!.offerId!!, card)
                .compose(sAsync())
                .doOnSubscribe {
                    viewState.changeCanOrderState(false)
                    viewState.showLoading()
                }
                .doFinally { viewState.dismissLoading() }
                .subscribe(
                        {

                            if (it.link != null) {
                                viewState.showOrderWeb(it.link)
                            } else if (it.success != null) {
                                viewState.showOrderSuccess(it.success)
                            }
                        },
                        {
                            viewState.changeCanOrderState(true)
                            if (it is CardConfirmThrowable) {
                                viewState.confirmPayment(it.card)
                            } else {
                                viewState.showError(R.string.error_network)
                                it.printStackTrace()
                            }
                        }
                )
    }

}