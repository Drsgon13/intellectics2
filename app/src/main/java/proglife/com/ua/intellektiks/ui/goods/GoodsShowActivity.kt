package proglife.com.ua.intellektiks.ui.goods

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
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlaybackException.TYPE_UNEXPECTED
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import kotlinx.android.synthetic.main.activity_goods_show_alt.*
import kotlinx.android.synthetic.main.content_bottom_sheet.*
import kotlinx.android.synthetic.main.content_bottom_sheet.view.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.exo_playback_control_view.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.data.models.*
import proglife.com.ua.intellektiks.extensions.DownloadService
import proglife.com.ua.intellektiks.ui.base.BaseActivity
import proglife.com.ua.intellektiks.ui.viewer.ViewerTxtActivity
import proglife.com.ua.intellektiks.utils.ExoUtils

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class GoodsShowActivity : BaseActivity(), GoodsShowView {

    @InjectPresenter
    lateinit var presenter: GoodsShowPresenter

    private var mFullScreenDialog: Dialog? = null
//    private lateinit var mMediaObjectAdapter: MediaObjectAdapter
//    private lateinit var mMarkerAdapter: MarkerAdapter

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


        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)
//        mMediaObjectAdapter = MediaObjectAdapter(object : MediaObjectAdapter.OnSelectMediaObjectListener {
//            override fun onDownload(mediaObject: MediaObject) {
//                presenter.download(mediaObject)
//            }
//
//            override fun onSelect(mediaObject: MediaObject) {
//                if (mediaObject.type == MediaObject.Type.PLAYER) {
//                    presenter.play(mediaObject, 0)
//                } else {
//                    val intent = MediaViewer.open(this@GoodsShowActivity, mediaObject)
//                    if (intent != null) {
//                        startActivity(intent)
//                        withStartAnimation()
//                    } else {
//                        Snackbar.make(coordinator, R.string.error_format, Snackbar.LENGTH_LONG).show()
//                    }
//                }
//            }
//        })
//        mMediaObjectAdapter.setHasStableIds(true)
        rvMediaObjects.layoutManager = LinearLayoutManager(this)
        rvMediaObjects.addItemDecoration(divider)
//        rvMediaObjects.adapter = mMediaObjectAdapter
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

    }


    override fun showVideo(mediaSource: DynamicConcatenatingMediaSource) {
        var currentPosition = 0
        var seekTo: Long = 0
        if(exoPlay!!.player !=null){
            currentPosition = exoPlay.player.currentWindowIndex
            seekTo = exoPlay.player.currentPosition
            exoPlay.player.release()
        }

        mediaContainer.visibility = VISIBLE
        val player = ExoUtils.initExoPlayerFactory(this)
        exoPlay!!.player = player
        player.addListener(object : Player.DefaultEventListener() {
            override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
                presenter.checkType(exoPlay.player.currentWindowIndex)
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if(playWhenReady)
                    presenter.startReminder()
                else presenter.clearTimer()

                if(playbackState == Player.STATE_IDLE)
                    presenter.checkSource(player.currentWindowIndex, applicationContext)

            }

            override fun onPlayerError(error: ExoPlaybackException) {
                if(error.type == TYPE_UNEXPECTED
                        && error.unexpectedException.stackTrace[0].fileName!!.contentEquals("MediaCodec.java"))
                    showError(R.string.error_play)
                presenter.setErrorPlay(player.currentWindowIndex)
                error.printStackTrace()
                presenter.clearTimer()

                player.playWhenReady = false
                exo_pause.visibility = GONE
                exo_play.visibility = VISIBLE

            }
        })

        exo_pause.setOnClickListener {
            player.playWhenReady = false
            it.visibility = GONE
            exo_play.visibility = VISIBLE
        }
        exo_play.setOnClickListener {
            player.playWhenReady = true
            it.visibility = GONE
            exo_pause.visibility = VISIBLE
        }
        player.prepare(mediaSource)
        player.seekTo(currentPosition, seekTo)
        initFullscreenDialog()
        initFullscreenButton()
    }

    override fun positionLesson() {
        presenter.reminder(exoPlay.player.currentWindowIndex, exoPlay.player.currentPosition)
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
        mFullScreenIcon.setImageResource(R.drawable.ic_fullscreen)
    }

    override fun hideMarker(marker: Marker){
//        mMarkerAdapter.hide(marker)
    }

    override fun showGoods(item: Goods, mList: List<MediaObject>) {

        if(item.togglesMassive !=null && item.togglesMassive!!.isNotEmpty()) {
//            mMarkerAdapter = MarkerAdapter(item.togglesMassive.toMutableList(), object : MarkerAdapter.OnClickMarker {
//
//                override fun onDelete(marker: Marker) {
//                    presenter.deleteMarker(marker)
//                }
//
//                override fun onContinue(marker: Marker) {
//                    presenter.playMarker(marker)
//                }
//
//                override fun onNo(marker: Marker) {
//                    hideMarker(marker)
//                }
//
//            })
//            rvMarker.layoutManager = LinearLayoutManager(this)
//            rvMarker.adapter = mMarkerAdapter
//            rvMarker.visibility = VISIBLE
        } else rvMarker.visibility = GONE
        // ???????????????????? ???????????????? ID ???? MediaObject ???? ???????????????? ?????????? ?????????????? ?? ????????????
        val pi = createPendingResult(DownloadService.REQUEST_CODE, Intent(), 0)
        val intent = Intent(this, DownloadService::class.java)
                .putExtra(DownloadService.MEDIA_OBJECT_IDS, mList.map { it.id }.toLongArray())
                .putExtra(DownloadService.PENDING_INTENT, pi)
        startService(intent)

        val playerList = mList.filter { it.type == MediaObject.Type.PLAYER && it.fileType != FileType.HLS }
        val size = playerList.fold(0, { acc, mediaObject -> acc + mediaObject.size.toInt() })
        val sizeText = if (size >= 1000)
            getString(R.string.file_download_all_gb, size.toFloat() / 1000)
        else getString(R.string.file_download_all_mb, size)
        btnDownloadAll.text = sizeText
        btnDownloadAll.visibility = if (size > 0) View.VISIBLE else View.GONE
        presenter.initDataSource(applicationContext)
//
//        if(item.informationForPersonal.isNotBlank()) {
//            btnShowDescription.visibility = View.VISIBLE
//            btnShowDescription.setOnClickListener {
//                startActivity(Intent(this, ViewerTxtActivity::class.java)
//                        .putExtra(Constants.Field.TITLE, getString(R.string.description))
//                        .putExtra(Constants.Field.CONTENT, item.informationForPersonal))
//                withStartAnimation()
//            }
//        }

//        mMediaObjectAdapter.show(mList)
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

    override fun seekTo(position: Int, seek: Long) {
        exoPlay.player.seekTo(position, seek)

        rvMediaObjects.scrollToPosition(0)
        exoPlay.player.playWhenReady = true
        exo_play.visibility = GONE
        exo_pause.visibility = VISIBLE
        innerAppBar.setExpanded(true, true)
    }

    override fun notifyItemChanged(index: Int) {
//        mMediaObjectAdapter.notifyItemChanged(index)
        presenter.checkDownload(this, index, exoPlay.player!!.currentWindowIndex)
    }

    /**
     * ???????????????????? ???????? ?????? ???????????????????? ?? ??????????????
     */
    override fun startDownload(mediaObject: MediaObject) {
        startService(Intent(this@GoodsShowActivity, DownloadService::class.java)
                .putExtra(DownloadService.MEDIA_OBJECT, mediaObject))
    }

    override fun notifyDataSetChanged() {
//        mMediaObjectAdapter.notifyDataSetChanged()
    }

    override fun startCommonDownload(mediaObject: MediaObject) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "?????????????????? ?? ${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}${mediaObject.getFileName()}", Toast.LENGTH_LONG).show()
            startService(Intent(this@GoodsShowActivity, DownloadService::class.java)
                    .putExtra(DownloadService.MEDIA_OBJECT, mediaObject))
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 9999)
        }
    }

    override fun selectItem(mediaObject: MediaObject) {
//        mMediaObjectAdapter.selectItem(mediaObject)
    }

    override fun showError(res: Int) {
        Snackbar.make(coordinator, res, Snackbar.LENGTH_LONG).show()
    }
}