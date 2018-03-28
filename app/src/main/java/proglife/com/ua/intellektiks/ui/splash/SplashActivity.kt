package proglife.com.ua.intellektiks.ui.splash

import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.ui.auth.AuthActivity
import proglife.com.ua.intellektiks.ui.base.BaseActivity
import proglife.com.ua.intellektiks.ui.main.MainActivity

/**
 * Created by Evhenyi Shcherbyna on 22.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class SplashActivity: BaseActivity(), SplashView {

    @InjectPresenter lateinit var presenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        presenter.init()
    }

    override fun showAuth() {
        startActivity(Intent(this, AuthActivity::class.java))
        withStartAnimation()
    }

    override fun showMain() {
        startActivity(Intent(this, MainActivity::class.java))
        withStartAnimation()
    }

}