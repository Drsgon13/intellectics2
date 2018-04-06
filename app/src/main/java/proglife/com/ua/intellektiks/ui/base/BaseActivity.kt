package proglife.com.ua.intellektiks.ui.base

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.Toolbar
import android.view.ViewStub
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.ui.auth.AuthActivity
import proglife.com.ua.intellektiks.ui.common.CommonPresenter
import proglife.com.ua.intellektiks.ui.common.CommonView
import proglife.com.ua.intellektiks.ui.profile.ProfileActivity
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 22.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
abstract class BaseActivity: MvpAppCompatActivity(), CommonView {

    lateinit var toolbar: Toolbar

    @InjectPresenter
    lateinit var commonPresenter: CommonPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setCustomView(layoutResID: Int, containerResID: Int = R.layout.content_main) {
        super.setContentView(containerResID)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val vs = findViewById<ViewStub>(R.id.view_stub)
        vs.layoutResource = layoutResID
        vs.inflate()
    }

    fun withStartAnimation() {
        overridePendingTransition(R.anim.right_in, R.anim.left_out)
    }

    fun withBackAnimation() {
        overridePendingTransition(R.anim.left_in, R.anim.right_out)
    }

    override fun showProfile() {
        startActivity(Intent(this, ProfileActivity::class.java))
        withStartAnimation()
    }

    override fun showAuth(clearStack: Boolean) {
        startActivity(Intent(this, AuthActivity::class.java))
        withBackAnimation()
        if (clearStack) finishAffinity()
    }

}