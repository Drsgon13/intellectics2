package proglife.com.ua.intellektiks.ui.goods

import android.app.Dialog
import android.net.Uri
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View.GONE
import android.view.View.VISIBLE
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
import kotlinx.android.synthetic.main.exo_playback_control_view.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.data.models.FileType
import proglife.com.ua.intellektiks.data.models.Goods
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.extensions.DownloadService
import proglife.com.ua.intellektiks.extensions.DownloadableFile
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
        mMediaObjectAdapter = MediaObjectAdapter(object : MediaObjectAdapter.OnSelectMediaObjectListener {
            override fun onDownload(mediaObject: MediaObject) {
                startService(Intent(this@GoodsShowActivity, DownloadService::class.java)
                        .putExtra(DownloadService.MEDIA_OBJECT, mediaObject))
            }
        })
        rvMediaObjects.layoutManager = LinearLayoutManager(this)
        rvMediaObjects.addItemDecoration(divider)
        rvMediaObjects.adapter = mMediaObjectAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DownloadService.CODE_UPDATE_STATE) {
            val parcelables: Array<Parcelable>? = data?.getParcelableArrayExtra(DownloadService.FILES)
            val files = parcelables?.map { it as DownloadableFile }
            presenter.progress(files)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        withBackAnimation()
    }

    override fun onPause() {
        super.onPause()
        if (exoPlay.player != null && exoPlay!!.player.playWhenReady) {
            exoPlay.player.playWhenReady = false
        }
        if(mFullScreenDialog.isShowing)
            mFullScreenDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlay.player?.release()
    }

    override fun showInfo(item: GoodsPreview) {

    }

    override fun showLoading() {
        pbLoading.show()
    }

    override fun dismissLoading() {
        pbLoading.hide()
    }

    override fun hideVideo() {
        mediaContainer.visibility = GONE
    }

    override fun showVideo(mediaSource: ConcatenatingMediaSource) {
        mediaContainer.visibility = VISIBLE
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        val loadControl = DefaultLoadControl()
        val player = ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(this), trackSelector, loadControl)
        exoPlay.hideController()
        exoPlay!!.player = player
        exoPlay.setPlaybackPreparer { exoPlay.showController() }


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
        mFullScreenDialog.setOnDismissListener {  closeFullscreenDialog() }
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
        scroll.smoothScrollTo(0,0)
        mFullScreenIcon.setImageResource(R.drawable.ic_fullscreen)
    }

    override fun showGoods(item: Goods) {
        val pi = createPendingResult(DownloadService.CODE_UPDATE_STATE, Intent(), 0)
        val intent = Intent(this, DownloadService::class.java)
                .putExtra(DownloadService.MEDIA_OBJECT_IDS, item.mediaObjects.map { it.id }.toLongArray())
                .putExtra(DownloadService.PENDING_INTENT,pi )
        startService(intent)

        presenter.initDataSourse(this, item.mediaObjects)
        tvName.text = item.name
        mMediaObjectAdapter.show(item.getMediaObjects(FileType.HLS, FileType.MP4, FileType.MP3))
    }

    override fun showProgress(count: Int, total: Int, progress: Int?) {
        if (total == 0) return
        if (progress != null) {
            tvProgress.text = getString(R.string.file_progress, count, total, progress)
        } else {
            tvProgress.text = getString(R.string.file_progress_complete, count, total)
        }
    }
}