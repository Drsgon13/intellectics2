package proglife.com.ua.intellektiks.ui.notifications.list

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_notification_list.*
import kotlinx.android.synthetic.main.content_main.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.data.models.NotificationMessagePreview
import proglife.com.ua.intellektiks.ui.base.BaseActivity
import proglife.com.ua.intellektiks.ui.notifications.show.NotificationShowActivity

/**
 * Created by Evhenyi Shcherbyna on 07.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class NotificationListActivity: BaseActivity(), NotificationListView {

    @InjectPresenter
    lateinit var mPresenter: NotificationListPresenter

    lateinit var mAdapter: NotificationPreviewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_notification_list)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setTitle(R.string.nav_notifications)

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)
        mAdapter = NotificationPreviewsAdapter(mPresenter)
        rvNotifications.addItemDecoration(divider)
        rvNotifications.layoutManager = LinearLayoutManager(this)
        rvNotifications.adapter = mAdapter
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

    override fun showNeedAuth() {
        tvMessage.visibility = View.VISIBLE
    }

    override fun showError(res: Int) {
        Snackbar.make(coordinator, res, Snackbar.LENGTH_LONG).show()
    }

    override fun showNotifications(list: List<NotificationMessagePreview>) {
        tvMessage.visibility = View.GONE
        mAdapter.show(list)
    }

    override fun showNotification(item: NotificationMessagePreview) {0
        startActivity(Intent(this, NotificationShowActivity::class.java)
                .putExtra(Constants.Field.NOTIFICATION, item))
        withStartAnimation()
    }
}