package proglife.com.ua.intellektiks.ui.content

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SimpleItemAnimator
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.android.synthetic.main.activity_content.*
import kotlinx.android.synthetic.main.content_bottom_sheet.*
import kotlinx.android.synthetic.main.content_bottom_sheet.view.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.exo_playback_control_view.view.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.data.models.*
import proglife.com.ua.intellektiks.extensions.DownloadService
import proglife.com.ua.intellektiks.ui.base.BaseActivity
import proglife.com.ua.intellektiks.ui.content.adapters.ContentAdapter
import proglife.com.ua.intellektiks.ui.content.holders.HeaderViewHolder
import proglife.com.ua.intellektiks.ui.content.media.MediaViewer
import proglife.com.ua.intellektiks.ui.viewer.ViewerTxtActivity
import proglife.com.ua.intellektiks.utils.ExoUtils

/**
 * Created by Evhenyi Shcherbyna on 18.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class ContentActivity : BaseActivity(), ContentView {

    @InjectPresenter
    lateinit var presenter: ContentPresenter

    @ProvidePresenter
    fun providePresenter() = ContentPresenter(
            intent.getParcelableExtra(Constants.Field.GOODS_PREVIEW),
            intent.getParcelableExtra(Constants.Field.LESSONS_PREVIEW)
    )

    private var mFullScreenDialog: Dialog? = null
    private lateinit var mContentAdapter: ContentAdapter
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<RelativeLayout>

    private lateinit var exoPlayerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_content)

        exoPlayerView = PlayerView(this)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setTitle(R.string.user_goods)

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)
        mContentAdapter = ContentAdapter(
                onMediaObjectAction = object : ContentAdapter.OnMediaObjectAction {
                    override fun onDownload(mediaObject: MediaObject) {
                        presenter.download(mediaObject)
                    }

                    override fun onSelect(mediaObject: MediaObject) {
                        if (mediaObject.type == MediaObject.Type.PLAYER) {
                            presenter.play(mediaObject, 0)
                        } else {
                            val intent = MediaViewer.open(this@ContentActivity, mediaObject)
                            if (intent != null) {
                                startActivity(intent)
                                withStartAnimation()
                            } else {
                                Snackbar.make(coordinator, R.string.error_format, Snackbar.LENGTH_LONG).show()
                            }
                        }
                    }
                },
                onHeaderAction = object : HeaderViewHolder.OnHeaderAction {
                    override fun showDescription(content: String) {
                        startActivity(Intent(this@ContentActivity, ViewerTxtActivity::class.java)
                                .putExtra(Constants.Field.TITLE, getString(R.string.description))
                                .putExtra(Constants.Field.CONTENT, content))
                        withStartAnimation()
                    }
                }
        )
        rvContent.layoutManager = LinearLayoutManager(this)
        rvContent.addItemDecoration(divider)
        rvContent.adapter = mContentAdapter
        (rvContent.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onPause() {
        super.onPause()
        if (exoPlayerView.player != null && exoPlayerView.player.playWhenReady) {
            exoPlayerView.player.playWhenReady = false
        }
        mFullScreenDialog?.let {
            if (it.isShowing) it.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayerView.player?.release()
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

    override fun showPreview(previewModel: Any?) {
        mContentAdapter.showHeader {
            title = when (previewModel) {
                is LessonPreview -> previewModel.name
                is GoodsPreview -> previewModel.name
                else -> "Unknown"
            }
        }
    }

    override fun showLoading() {
        pbLoading.show()
    }

    override fun dismissLoading() {
        pbLoading.hide()
    }

    override fun showContent(content: Content, list: List<MediaObject>) {
        // Отправляем перечень ID от MediaObject за которыми хотим следить в сервис
        val pi = createPendingResult(DownloadService.REQUEST_CODE, Intent(), 0)
        val intent = Intent(this, DownloadService::class.java)
                .putExtra(DownloadService.MEDIA_OBJECT_IDS, list.map { it.id }.toLongArray())
                .putExtra(DownloadService.PENDING_INTENT, pi)
        startService(intent)

        presenter.initDataSource(applicationContext)

        mContentAdapter.showHeader { description = content.description }
        mContentAdapter.showMedia(list)

    }

    override fun showReports(show: Boolean, messages: List<ReportMessage>) {
        mContentAdapter.showReports(show, messages)
    }

    override fun selectItem(mediaObject: MediaObject) {
        mContentAdapter.selectItem(mediaObject)
    }

    override fun showNoData() {
        Snackbar.make(coordinator, R.string.error_network, Snackbar.LENGTH_LONG).show()
    }

    override fun showError(res: Int) {
        Snackbar.make(coordinator, res, Snackbar.LENGTH_LONG).show()
    }

    //--------------------------------------------------------------------------
    // REMINDER
    //--------------------------------------------------------------------------

    override fun requestPlayerPosition() {
        presenter.addReminderMark(exoPlayerView.player.currentWindowIndex, exoPlayerView.player.currentPosition)
    }

    //--------------------------------------------------------------------------
    // PLAYER
    //--------------------------------------------------------------------------

    override fun onEmptyMediaList() {

    }

    override fun showVideo(dynamicMediaSource: DynamicConcatenatingMediaSource) {
        var currentPosition = 0
        var seekTo: Long = 0
        if (exoPlayerView.player != null) {
            currentPosition = exoPlayerView.player.currentWindowIndex
            seekTo = exoPlayerView.player.currentPosition
            exoPlayerView.player.release()
        }

        val player = ExoUtils.initExoPlayerFactory(this)
        exoPlayerView.player = player

        mContentAdapter.showPlayerView(exoPlayerView)

        player.addListener(object : Player.DefaultEventListener() {
            override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
                super.onTracksChanged(trackGroups, trackSelections)
                presenter.playNextOrPrev(exoPlayerView.player.currentWindowIndex)
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playWhenReady)
                    presenter.startReminder()
                else presenter.clearTimer()

                if(playbackState == Player.STATE_IDLE)
                    presenter.checkSource(player.currentWindowIndex, applicationContext)
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                if(error.type == ExoPlaybackException.TYPE_UNEXPECTED
                        && error.unexpectedException.stackTrace[0].fileName!!.contentEquals("MediaCodec.java"))
                    showError(R.string.error_play)
                presenter.setErrorPlayPosition(player.currentWindowIndex)
                error.printStackTrace()
                presenter.clearTimer()

                player.playWhenReady = false
                exoPlayerView.exo_pause.visibility = GONE
                exoPlayerView.exo_play.visibility = VISIBLE

            }
        })

        exoPlayerView.exo_pause.setOnClickListener {
            player.playWhenReady = false
            it.visibility = GONE
            exoPlayerView.exo_play.visibility = VISIBLE
        }
        exoPlayerView.exo_play.setOnClickListener {
            player.playWhenReady = true
            it.visibility = GONE
            exoPlayerView.exo_pause.visibility = VISIBLE
        }

        player.prepare(dynamicMediaSource)
        player.seekTo(currentPosition, seekTo)
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
        exoPlayerView.mFullScreenButton.setOnClickListener {
            mFullScreenDialog?.let {
                if (!it.isShowing)
                    openFullscreenDialog()
                else
                    closeFullscreenDialog()
            }
        }
    }

    private fun openFullscreenDialog() {
        exoPlayerView.mFullScreenIcon.setImageResource(R.drawable.ic_fullscreen_expand)
        (exoPlayerView.parent as ViewGroup).removeView(exoPlayerView)
        mContentAdapter.removePlayerView()
        mFullScreenDialog?.addContentView(exoPlayerView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        mFullScreenDialog?.show()
    }

    private fun closeFullscreenDialog() {
        (exoPlayerView.parent as ViewGroup).removeView(exoPlayerView)
        mContentAdapter.showPlayerView(exoPlayerView)
        mFullScreenDialog?.dismiss()
        exoPlayerView.mFullScreenIcon.setImageResource(R.drawable.ic_fullscreen)
    }

    override fun seekTo(index: Int, position: Long) {
        rvContent.scrollToPosition(mContentAdapter.getPlayerPosition())
        exoPlayerView.player.seekTo(index, 0)
        exoPlayerView.player.playWhenReady = true

        exoPlayerView.exo_play.visibility = GONE
        exoPlayerView.exo_pause.visibility = VISIBLE
    }

    override fun checkContent(isAudio: Boolean) {
        if (isAudio && mFullScreenDialog != null && mFullScreenDialog!!.isShowing)
            closeFullscreenDialog()
        else if (mFullScreenDialog!!.isShowing) return

        exoPlayerView.controllerShowTimeoutMs = if (isAudio) Int.MAX_VALUE else 0
        exoPlayerView.controllerHideOnTouch = !isAudio
        exoPlayerView.mFullScreenButton.visibility = if (isAudio) GONE else VISIBLE
    }

    //--------------------------------------------------------------------------
    // DOWNLOADER
    //--------------------------------------------------------------------------

    override fun startDownload(mediaObject: MediaObject) {
        startService(Intent(this@ContentActivity, DownloadService::class.java)
                .putExtra(DownloadService.MEDIA_OBJECT, mediaObject))
    }

    override fun startCommonDownload(mediaObject: MediaObject) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + mediaObject.getFileName()
            Toast.makeText(this, getString(R.string.file_save_to, path), Toast.LENGTH_LONG).show()
            startService(Intent(this@ContentActivity, DownloadService::class.java)
                    .putExtra(DownloadService.MEDIA_OBJECT, mediaObject))
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 9999)
        }
    }

    override fun showProgress(current: Int, total: Int, progress: Int?) {
        if (progress != null) {
            if (mBottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            bottomSheetLayout.tvProgress.text = getString(R.string.file_progress, current, total, progress)
        } else {
            bottomSheetLayout.tvProgress.text = getString(R.string.file_progress_complete, current, total)
        }
    }

    override fun notifyItemChanged(index: Int) {
        mContentAdapter.notifyMediaItemChanged(index)
        presenter.checkDownload(this, index, exoPlayerView.player!!.currentWindowIndex)
    }

    override fun notifyDataSetChanged() {
        mContentAdapter.notifyDataSetChanged()
    }

}