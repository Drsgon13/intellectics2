package proglife.com.ua.intellektiks.ui.goods

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import kotlinx.android.synthetic.main.activity_goods_show_alt.*
import kotlinx.android.synthetic.main.content_bottom_sheet.*
import kotlinx.android.synthetic.main.content_bottom_sheet.view.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.exo_playback_control_view.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.data.models.Goods
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.extensions.DownloadService
import proglife.com.ua.intellektiks.extensions.DownloadableFile
import proglife.com.ua.intellektiks.ui.base.BaseActivity
import proglife.com.ua.intellektiks.ui.base.media.MediaObjectAdapter
import proglife.com.ua.intellektiks.ui.base.media.MediaViewer
import proglife.com.ua.intellektiks.utils.ExoUtils

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class GoodsShowActivity : BaseActivity(), GoodsShowView {

    @InjectPresenter
    lateinit var presenter: GoodsShowPresenter

    private var mFullScreenDialog: Dialog? = null
    private lateinit var mMediaObjectAdapter: MediaObjectAdapter

    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<RelativeLayout>

    @ProvidePresenter
    fun providePresenter(): GoodsShowPresenter {
        return GoodsShowPresenter(intent.getParcelableExtra(Constants.Field.GOODS_PREVIEW))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_goods_show_alt)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setTitle(R.string.user_goods)

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)

        findViewById<View>(com.google.android.exoplayer2.ui.R.id.exo_content_frame).visibility = GONE

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)
        mMediaObjectAdapter = MediaObjectAdapter(object : MediaObjectAdapter.OnSelectMediaObjectListener {
            override fun onDownload(mediaObject: MediaObject) {
                presenter.download(mediaObject)
            }

            override fun onSelect(mediaObject: MediaObject) {
                if (mediaObject.type == MediaObject.Type.PLAYER) {
                    presenter.play(mediaObject)
                } else {
                    val intent = MediaViewer.open(this@GoodsShowActivity, mediaObject)
                    if (intent != null) {
                        startActivity(intent)
                        withStartAnimation()
                    } else {
                        Snackbar.make(coordinator, R.string.error_format, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })
//        mMediaObjectAdapter.setHasStableIds(true)
        rvMediaObjects.layoutManager = LinearLayoutManager(this)
        rvMediaObjects.addItemDecoration(divider)
        rvMediaObjects.adapter = mMediaObjectAdapter
        (rvMediaObjects.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        btnDownloadAll.setOnClickListener {
            presenter.downloadAll()
        }

        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DownloadService.REQUEST_CODE) {
            presenter.onServiceCallback(resultCode, data)
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
        mFullScreenDialog?.let {
            if (it.isShowing) it.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlay.player?.release()
    }

    override fun showInfo(item: GoodsPreview) {
        tvName.text = item.name
    }

    override fun showLoading() {
        pbLoading.show()
    }

    override fun dismissLoading() {
        pbLoading.hide()
    }

    override fun emptyList() {
        mediaContainer.visibility = GONE
    }

    /**
     *
     */
    override fun checkContent(isAudio: Boolean) {
        if (isAudio && mFullScreenDialog != null && mFullScreenDialog!!.isShowing)
            closeFullscreenDialog()
        else if (mFullScreenDialog!!.isShowing) return

        exoPlay.controllerShowTimeoutMs = if (isAudio) Int.MAX_VALUE else 0
        exoPlay.controllerHideOnTouch = !isAudio
        mFullScreenButton.visibility = if (isAudio) GONE else VISIBLE

        val view = findViewById<AspectRatioFrameLayout>(com.google.android.exoplayer2.ui.R.id.exo_content_frame)
        view.visibility = if (isAudio) GONE else VISIBLE
        mediaContainer.layoutParams.height = if (isAudio) ViewGroup.LayoutParams.WRAP_CONTENT
        else resources.getDimensionPixelSize(R.dimen.height)
        mediaContainer.requestLayout()
        mediaContainer.invalidate()
    }


    override fun showVideo(mediaSource: ConcatenatingMediaSource) {
        mediaContainer.visibility = VISIBLE
        val player = ExoUtils.initExoPlayerFactory(this)
        exoPlay!!.player = player

        player.addListener(object : Player.DefaultEventListener() {
            override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
                super.onTracksChanged(trackGroups, trackSelections)
                presenter.checkType(exoPlay.player.currentWindowIndex)
            }
        })

        player.prepare(mediaSource)
        initFullscreenDialog()
        initFullscreenButton()
    }

    private fun initFullscreenDialog() {
        mFullScreenDialog = object : Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            override fun onBackPressed() {
                mFullScreenDialog?.let {
                    if (it.isShowing) closeFullscreenDialog()
                }
                super.onBackPressed()
            }
        }
        mFullScreenDialog?.setOnDismissListener { closeFullscreenDialog() }
    }

    private fun initFullscreenButton() {
        mFullScreenButton.setOnClickListener {
            mFullScreenDialog?.let {
                if (!it.isShowing)
                    openFullscreenDialog()
                else
                    closeFullscreenDialog()
            }
        }
    }

    private fun openFullscreenDialog() {
        mFullScreenIcon.setImageResource(R.drawable.ic_fullscreen_expand)
        mediaContainer.removeView(exoPlay)
        mFullScreenDialog?.addContentView(exoPlay, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        mFullScreenDialog?.show()
    }

    private fun closeFullscreenDialog() {
        (exoPlay!!.parent as ViewGroup).removeView(exoPlay)
        mediaContainer.addView(exoPlay)
        mFullScreenDialog?.dismiss()
//        scroll.smoothScrollTo(0,0)
        mFullScreenIcon.setImageResource(R.drawable.ic_fullscreen)
    }

    /**
     *
     */
    override fun showGoods(item: Goods, mList: List<MediaObject>) {
        // Отправляем перечень ID от MediaObject за которыми хотим следить в сервис
        val pi = createPendingResult(DownloadService.REQUEST_CODE, Intent(), 0)
        val intent = Intent(this, DownloadService::class.java)
                .putExtra(DownloadService.MEDIA_OBJECT_IDS, mList.map { it.id }.toLongArray())
                .putExtra(DownloadService.PENDING_INTENT, pi)
        startService(intent)

        val playerList = mList.filter { it.type == MediaObject.Type.PLAYER }
        val size = playerList.fold(0, { acc, mediaObject -> acc + mediaObject.size.toInt() })
        val sizeText = if (size >= 1000)
            getString(R.string.file_download_all_gb, size.toFloat() / 1000)
        else getString(R.string.file_download_all_mb, size)
        btnDownloadAll.text = sizeText

        presenter.initDataSource(this, playerList)
        mMediaObjectAdapter.show(mList)
    }

    override fun showProgress(count: Int, total: Int, progress: Int?) {
        if (progress != null) {
            if (mBottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            bottomSheetLayout.tvProgress.text = getString(R.string.file_progress, count, total, progress)
        } else {
            bottomSheetLayout.tvProgress.text = getString(R.string.file_progress_complete, count, total)
        }
    }

    override fun showNoData() {
        Snackbar.make(coordinator, R.string.error_network, Snackbar.LENGTH_LONG).show()
    }

    override fun seekTo(position: Int) {
        exoPlay.player.seekTo(position, 0)
        rvMediaObjects.scrollToPosition(0)
        innerAppBar.setExpanded(true, true)
    }

    override fun notifyItemChanged(index: Int) {
        mMediaObjectAdapter.notifyItemChanged(index)
    }

    /**
     * Отправляем файл для скачивания в очередь
     */
    override fun startDownload(mediaObject: MediaObject) {
        startService(Intent(this@GoodsShowActivity, DownloadService::class.java)
                .putExtra(DownloadService.MEDIA_OBJECT, mediaObject))
    }

    override fun notifyDataSetChanged() {
        mMediaObjectAdapter.notifyDataSetChanged()
    }

    override fun startCommonDownload(mediaObject: MediaObject) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Сохранено в ${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}${mediaObject.getFileName()}", Toast.LENGTH_LONG).show()
            startService(Intent(this@GoodsShowActivity, DownloadService::class.java)
                    .putExtra(DownloadService.MEDIA_OBJECT, mediaObject))
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 9999)
        }
    }

    override fun selectItem(mediaObject: MediaObject) {
        mMediaObjectAdapter.selectItem(mediaObject)
    }
}