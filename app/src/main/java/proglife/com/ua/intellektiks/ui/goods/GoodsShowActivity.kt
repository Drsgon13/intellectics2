package proglife.com.ua.intellektiks.ui.goods

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import kotlinx.android.synthetic.main.activity_goods_show.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.exo_playback_control_view.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.data.models.FileType
import proglife.com.ua.intellektiks.data.models.Goods
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.ui.base.BaseActivity


/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class GoodsShowActivity: BaseActivity(), GoodsShowView {


    @InjectPresenter lateinit var presenter: GoodsShowPresenter


    private lateinit var mFullScreenDialog: Dialog
    private lateinit var mMediaObjectAdapter: MediaObjectAdapter

    @ProvidePresenter
    fun providePresenter(): GoodsShowPresenter {
        return GoodsShowPresenter(intent.getParcelableExtra(Constants.Field.GOODS_PREVIEW))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_goods_show)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setTitle(R.string.user_goods)

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)
        mMediaObjectAdapter = MediaObjectAdapter()
        rvMediaObjects.layoutManager = LinearLayoutManager(this)
        rvMediaObjects.addItemDecoration(divider)
        rvMediaObjects.adapter = mMediaObjectAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        withBackAnimation()
    }

    override fun showInfo(item: GoodsPreview) {

    }

    override fun showLoading() {
        pbLoading.show()
    }

    override fun dismissLoading() {
        pbLoading.hide()
    }

    override fun showVideo(mediaSource: ConcatenatingMediaSource) {
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        val loadControl = DefaultLoadControl()
        val player = ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(this), trackSelector, loadControl)
        exoPlay!!.player = player



        player.prepare(mediaSource)
        initFullscreenDialog()
        initFullscreenButton()
    }

    private fun initFullscreenDialog() {

        mFullScreenDialog = object : Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            override fun onBackPressed() {
                if (mFullScreenDialog.isShowing)
                    closeFullscreenDialog()
                super.onBackPressed()
            }
        }
    }

    private fun initFullscreenButton() {

        mFullScreenButton.setOnClickListener {
            if (!mFullScreenDialog.isShowing)
                openFullscreenDialog()
            else
                closeFullscreenDialog()
        }
    }

    private fun openFullscreenDialog() {

        mFullScreenIcon.setImageResource(R.drawable.ic_fullscreen_expand)
        mediaContainer.removeView(exoPlay)
        mFullScreenDialog.addContentView(exoPlay, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))


        mFullScreenDialog.show()
    }

    private fun closeFullscreenDialog() {
        (exoPlay!!.parent as ViewGroup).removeView(exoPlay)
        mediaContainer.addView(exoPlay)
        mFullScreenDialog.dismiss()
        mFullScreenIcon.setImageResource(R.drawable.ic_fullscreen)
    }

    override fun showGoods(item: Goods) {
        presenter.initDataSourse(this, item.mediaObjects)
        tvName.text = item.name
        mMediaObjectAdapter.show(item.getMediaObjects(FileType.HLS, FileType.MP4, FileType.MP3))
    }



}