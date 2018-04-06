package proglife.com.ua.intellektiks.ui.settings

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_settings.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.UserData
import proglife.com.ua.intellektiks.ui.base.BaseActivity

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class SettingsActivity: BaseActivity(), SettingsView {

    @InjectPresenter
    lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_settings)

        supportActionBar?.setTitle(R.string.nav_settings)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        btnLogout.setOnClickListener { commonPresenter.logout() }
        btnClear.setOnClickListener { presenter.clearCache() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        withBackAnimation()
    }

    override fun showData(userData: Pair<String?, UserData?>) {
        userData.first?.let { tvEmail.text = it }
        userData.second?.let {
            tvName.text = getString(R.string.profile_hello, it.fullName())
            Glide.with(this)
                    .load(it.image())
                    .into(ivPhoto)
        }
    }

    override fun showCacheSize(size: Long) {
        val textSize = if (size >= 1024 * 1024 * 1024)
            getString(R.string.cache_size_gb, size / 1024f / 1024f / 1024f)
        else getString(R.string.cache_size_mb, size / 1024 / 1024)
        tvCacheInfo.text = textSize
    }

    override fun showClearCacheLoading() {
        btnClear.visibility = View.GONE
        pbLoading.visibility = View.VISIBLE
    }

    override fun dismissClearCacheLoading() {
        btnClear.visibility = View.VISIBLE
        pbLoading.visibility = View.GONE
    }
}