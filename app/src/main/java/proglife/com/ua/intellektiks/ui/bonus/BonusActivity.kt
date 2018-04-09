package proglife.com.ua.intellektiks.ui.bonus

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import android.widget.RelativeLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_bonus.*
import kotlinx.android.synthetic.main.content_bottom_sheet.*
import kotlinx.android.synthetic.main.content_bottom_sheet.view.*
import kotlinx.android.synthetic.main.li_media_object_player.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.FileType
import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.extensions.DownloadService
import proglife.com.ua.intellektiks.extensions.DownloadableFile
import proglife.com.ua.intellektiks.ui.base.BaseActivity
import proglife.com.ua.intellektiks.ui.base.NavBaseActivity
import proglife.com.ua.intellektiks.utils.ExoUtils
import java.io.File

class BonusActivity: BaseActivity(), BonusView{

    @InjectPresenter lateinit var presenter: BonusPresenter

    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<RelativeLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_bonus)
        supportActionBar?.setTitle(R.string.nav_bonus)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        val spanable = SpannableString(getString(R.string.text_bonus))
        spanable.setSpan(StyleSpan(Typeface.BOLD), 0, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.text =  spanable

        tvName.text = getString(R.string.name_bonus)
        tvInfo.text = getString(R.string.file_info, "9")



        presenter.init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DownloadService.REQUEST_CODE) {
            presenter.onServiceCallback(resultCode, data)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun initPlayer(mediaObject: MediaObject){

        var file = mediaObject.url
        if(File("${filesDir}/c_${mediaObject.downloadableFile!!.id}.MP3").exists()){
            file = File("${filesDir}/c_${mediaObject.downloadableFile!!.id}").absolutePath
            mediaObject.downloadable = true
            mediaObject.downloadableFile!!.state = DownloadableFile.State.FINISHED
            updateStateItem(mediaObject)
        } else {
            val pi = createPendingResult(DownloadService.REQUEST_CODE, Intent(), 0)
            val intent = Intent(this, DownloadService::class.java)
                    .putExtra(DownloadService.MEDIA_OBJECT_IDS, longArrayOf(mediaObject.id))
                    .putExtra(DownloadService.PENDING_INTENT, pi)
            startService(intent)
            btnDownload.setOnClickListener { presenter.startDownload() }
        }

        val player = ExoUtils.initExoPlayerFactory(this)
        player.prepare(ExoUtils.buildMediaSource(ExoUtils.buildDataSourceFactory(this), Uri.parse(file), FileType.MP3))
        playController.player = player
        playController.showTimeoutMs = Int.MAX_VALUE
    }

    private fun showLoading(showLoading: Boolean){
        btnDownload.visibility = if (showLoading) View.GONE else View.VISIBLE
        pbDownload.visibility = if (showLoading) View.VISIBLE else View.GONE
    }

    private fun updateStateItem(mediaObject: MediaObject){
        val buttonColor = when (mediaObject.downloadableFile?.state) {
            DownloadableFile.State.FINISHED -> R.color.colorTitleGreenText
            DownloadableFile.State.FAILED -> R.color.colorTitleRedText
            else -> R.color.colorTitleBlueText
        }
        btnDownload.setColorFilter(ContextCompat.getColor(this, buttonColor))
    }

    override fun showProgress(count: Int, total: Int, progress: Int?) {
        if (progress != null) {
            showLoading(true)
            if (mBottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            bottomSheetLayout.tvProgress.text = getString(R.string.file_progress, count, total, progress)
        } else {
            bottomSheetLayout.tvProgress.text = getString(R.string.file_progress_complete, count, total)
            showLoading(false)
        }
    }

    override fun startDownload(mediaObject: MediaObject) {
        startService(Intent(this, DownloadService::class.java)
                .putExtra(DownloadService.MEDIA_OBJECT, mediaObject))
    }

    override fun onPause() {
        super.onPause()
        if(playController.player!=null)
            playController.player.playWhenReady = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        withBackAnimation()
    }

}