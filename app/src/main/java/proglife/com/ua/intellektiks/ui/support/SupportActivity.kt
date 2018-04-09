package proglife.com.ua.intellektiks.ui.support

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_support.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.Help
import proglife.com.ua.intellektiks.ui.base.BaseActivity
import proglife.com.ua.intellektiks.ui.base.NavBaseActivity

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class SupportActivity: BaseActivity(), SupportView {

    @InjectPresenter
    lateinit var presenter: SupportPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_support)

        supportActionBar?.setTitle(R.string.nav_support)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        presenter.loadHelp()
    }

    override fun showHelp(it: Help) {
        val mimeType = "text/html"
        val encoding = "UTF-8"
        webView.loadDataWithBaseURL("", it.content, mimeType, encoding, "")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        withBackAnimation()
    }
}