package proglife.com.ua.intellektiks.ui.notifications.show

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_notification_show.*
import kotlinx.android.synthetic.main.content_main.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.data.models.NotificationMessage
import proglife.com.ua.intellektiks.data.models.NotificationMessagePreview
import proglife.com.ua.intellektiks.ui.base.BaseActivity
import java.text.SimpleDateFormat

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
        return NotificationShowPresenter(intent.getParcelableExtra(Constants.Field.NOTIFICATION))
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_notification_show)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setTitle(R.string.nav_notifications)
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
        Snackbar.make(coordinator, res, Snackbar.LENGTH_LONG).show()
    }

    override fun showContent(it: NotificationMessagePreview, item: NotificationMessage) {
        webView.loadData("<p>${it.subject}</p>" + item.text, "text/html", "UTF-8")
    }

    override fun showItem(item: NotificationMessagePreview) {
    }

}