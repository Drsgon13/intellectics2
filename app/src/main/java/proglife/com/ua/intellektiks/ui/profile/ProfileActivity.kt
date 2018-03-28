package proglife.com.ua.intellektiks.ui.profile

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_profile.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.UserData
import proglife.com.ua.intellektiks.ui.base.BaseActivity

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class ProfileActivity: BaseActivity(), ProfileView {

    @InjectPresenter
    lateinit var presenter: ProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_profile)

        supportActionBar?.setTitle(R.string.profile)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        btnLogout.setOnClickListener { commonPresenter.logout() }
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

}