package proglife.com.ua.intellektiks.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.content_main.*
import proglife.com.ua.intellektiks.BuildConfig
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.ui.base.BaseActivity
import proglife.com.ua.intellektiks.ui.main.MainActivity
import proglife.com.ua.intellektiks.ui.remember.ForgotActivity

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class AuthActivity: BaseActivity(), AuthView {

    @InjectPresenter
    lateinit var presenter: AuthPresenter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_auth)

        if (BuildConfig.DEBUG) {
            etLogin.setText(BuildConfig.TEST_LOGIN)
            etPassword.setText(BuildConfig.TEST_PASSWORD)
        }

        supportActionBar?.setTitle(R.string.auth_title)
        btnSignIn.setOnClickListener {
            presenter.signIn(etLogin.text.toString(), etPassword.text.toString(), cbRemember.isChecked)
        }
        btnSkip.setOnClickListener { showNext() }
        btnForgot.setOnClickListener { showForgotScreen() }
    }

    override fun showError(message: String?) {
        if (message == null) return
        Snackbar.make(coordinator, message, Snackbar.LENGTH_LONG).show()
    }

    override fun showLoading() {
        showUiLoading(true)
    }

    override fun dismissLoading() {
        showUiLoading(false)
    }

    private fun showUiLoading(show: Boolean) {
        etLogin.isEnabled = show.not()
        etPassword.isEnabled = show.not()
        btnSignIn.visibility = if (show) View.GONE else View.VISIBLE
        btnSkip.visibility = if (show) View.GONE else View.VISIBLE
        btnForgot.visibility = if (show) View.GONE else View.VISIBLE
        pbLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showNext() {
        startActivity(Intent(this, MainActivity::class.java))
        withStartAnimation()
        finishAffinity()
    }

    private fun showForgotScreen() {
        startActivity(Intent(this, ForgotActivity::class.java))
        withStartAnimation()
    }

}