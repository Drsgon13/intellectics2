package proglife.com.ua.intellektiks.ui.notifications.show

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_notification_show.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.data.models.NotificationMessage
import proglife.com.ua.intellektiks.data.models.NotificationMessagePreview
import proglife.com.ua.intellektiks.ui.base.BaseActivity
import java.text.SimpleDateFormat
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.view.View
import proglife.com.ua.intellektiks.data.models.Card


/**
 * Created by Evhenyi Shcherbyna on 07.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class NotificationShowActivity: BaseActivity(), NotificationShowView {

    @InjectPresenter
    lateinit var mPresenter: NotificationShowPresenter

    @SuppressLint("SimpleDateFormat")
    private val mSDF = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @ProvidePresenter
    fun providePresenter(): NotificationShowPresenter {
        return NotificationShowPresenter(intent.getParcelableExtra(Constants.Field.NOTIFICATION),
                intent.getStringExtra(Constants.Field.ID_MESSAAGE),
                intent.getStringExtra(Constants.Field.TYPE))
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_notification_show)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setTitle(R.string.nav_notifications)

        fabBuy.setOnClickListener {
            mPresenter.makeOrder()
        }


    }

    override fun updateNotif() {
        sendBroadcast(Intent(Constants.Field.NOTIFICATION_UPDATE))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        withBackAnimation()
    }

    override fun showLoading() {
        pbLoading.show()
    }

    override fun dismissLoading() {
        pbLoading.hide()
    }

    override fun showError(res: Int) {
        Snackbar.make(innerCoordinator, res, Snackbar.LENGTH_LONG).show()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun showURL(url: String) {
        val notificationIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(notificationIntent)
        finish()
    }

    override fun showContent(it: NotificationMessagePreview, item: NotificationMessage) {
        webView.loadData("<p>${it.subject}</p>" + item.text, "text/html; charset=utf-8", "UTF-8")
    }

    override fun showItem(item: NotificationMessagePreview) {
    }

    override fun showOrderWeb(link: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(link)
        startActivity(i)
    }

    override fun showOrderSuccess(message: String) {
        Snackbar.make(innerCoordinator, message, Snackbar.LENGTH_LONG).show()
    }

    override fun changeCanOrderState(can: Boolean) {
        fabBuy.visibility = if (can) View.VISIBLE else View.GONE
    }

    override fun confirmPayment(card: Card) {
        AlertDialog.Builder(this)
                .setMessage(getString(R.string.payment_request_confirm, card.mask))
                .setPositiveButton(R.string.payment_confirm) { dialog, _ ->
                    mPresenter.makeOrder(card)
                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.payment_cancel)) { dialog, _ -> dialog.dismiss() }
                .show()
    }
}