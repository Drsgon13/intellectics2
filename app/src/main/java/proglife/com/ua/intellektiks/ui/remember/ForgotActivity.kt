package proglife.com.ua.intellektiks.ui.remember

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_forgot.*
import kotlinx.android.synthetic.main.content_main.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.ui.base.BaseActivity

/**
 * Created by Evhenyi Shcherbyna on 04.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class ForgotActivity: BaseActivity(), ForgotView {

    @InjectPresenter
    lateinit var presenter: ForgotPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_forgot)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setTitle(R.string.recovery_password)

        btnRecovery.setOnClickListener { presenter.recoveryPassword(etEmail.text.toString()) }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        withBackAnimation()
    }

    override fun showLoading() {
        showUiLoading(true)
    }

    override fun dismissLoading() {
        showUiLoading(false)
    }

    private fun showUiLoading(show: Boolean) {
        etEmail.isEnabled = show.not()
        btnRecovery.visibility = if (show) View.GONE else View.VISIBLE
        pbLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showError(message: String?) {
        if (message == null) return
        Snackbar.make(coordinator, message, Snackbar.LENGTH_LONG).show()
    }

    override fun showSuccess(email: String) {
        Toast.makeText(this, getString(R.string.recovery_password_success, email), Toast.LENGTH_LONG).show()
        onBackPressed()
    }
}