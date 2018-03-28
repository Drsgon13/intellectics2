package proglife.com.ua.intellektiks.ui.base

import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.content_navigation.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.ui.main.MainActivity
import proglife.com.ua.intellektiks.ui.support.SupportActivity

/**
 * Created by Evhenyi Shcherbyna on 22.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
abstract class NavBaseActivity : BaseActivity(), BaseView, NavigationView.OnNavigationItemSelectedListener {

    fun setCustomView(layoutResID: Int) {
        super.setCustomView(layoutResID, R.layout.content_navigation)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.app_name, R.string.app_name)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_user_goods -> {
                startActivity(Intent(this, MainActivity::class.java))
                withStartAnimation()
            }
            R.id.nav_support -> {
                startActivity(Intent(this, SupportActivity::class.java))
                withStartAnimation()
            }
            R.id.nav_logout -> commonPresenter.logout()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                commonPresenter.profile()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}