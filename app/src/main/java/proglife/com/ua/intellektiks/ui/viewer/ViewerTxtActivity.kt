package proglife.com.ua.intellektiks.ui.viewer

import android.annotation.SuppressLint
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_viewer_txt.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.ui.base.BaseActivity

/**
 * Created by Evhenyi Shcherbyna on 04.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class ViewerTxtActivity: BaseActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_viewer_txt)

        val title = intent.getStringExtra(Constants.Field.TITLE)
        val content = intent.getStringExtra(Constants.Field.CONTENT)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        supportActionBar?.title = title
        wvContent.loadData(content, "text/html; charset=utf-8", "UTF-8")
//        wvContent.settings.useWideViewPort = true
        wvContent.settings.javaScriptEnabled = true
//        wvContent.settings.loadWithOverviewMode = true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        withBackAnimation()
    }

}