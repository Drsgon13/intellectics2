package proglife.com.ua.intellektiks.ui.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.TaskStackBuilder
import com.arellomobile.mvp.presenter.InjectPresenter
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.ui.auth.AuthActivity
import proglife.com.ua.intellektiks.ui.base.BaseActivity
import proglife.com.ua.intellektiks.ui.main.MainActivity
import proglife.com.ua.intellektiks.ui.notifications.show.NotificationShowActivity

/**
 * Created by Evhenyi Shcherbyna on 22.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class SplashActivity: BaseActivity(), SplashView {

    @InjectPresenter lateinit var presenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        presenter.init(intent)
    }

    override fun showAuth() {
        startActivity(Intent(this, AuthActivity::class.java))
        withStartAnimation()
    }

    override fun showMain() {
        startActivity(Intent(this, MainActivity::class.java))
        withStartAnimation()
    }

    override fun openBrowser(url: String) {
        val notificationIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(notificationIntent)
    }

    override fun showNotification(id: String) {
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntent(Intent(this, MainActivity::class.java))
        stackBuilder.addNextIntent(
                Intent(this, NotificationShowActivity::class.java)
                .putExtra(Constants.Field.ID_MESSAAGE, id)
                .putExtra(Constants.Field.TYPE, "2")
        )
        stackBuilder.startActivities()
    }
}