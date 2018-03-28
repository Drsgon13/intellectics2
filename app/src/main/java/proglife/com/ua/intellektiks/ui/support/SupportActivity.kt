package proglife.com.ua.intellektiks.ui.support

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.ui.base.NavBaseActivity

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class SupportActivity: NavBaseActivity(), SupportView {

    @InjectPresenter
    lateinit var presenter: SupportPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_support)

        supportActionBar?.setTitle(R.string.nav_support)
    }

}