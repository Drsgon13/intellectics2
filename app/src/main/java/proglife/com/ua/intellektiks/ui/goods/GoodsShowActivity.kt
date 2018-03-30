package proglife.com.ua.intellektiks.ui.goods

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_goods_show.*
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

    @InjectPresenter
    lateinit var presenter: GoodsShowPresenter

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

    override fun showInfo(item: GoodsPreview) {

    }

    override fun showLoading() {
        pbLoading.show()
    }

    override fun dismissLoading() {
        pbLoading.hide()
    }

    override fun showGoods(item: Goods) {
        val pi = createPendingResult(DownloadService.CODE_UPDATE_STATE, Intent(), 0)
        val intent = Intent(this, DownloadService::class.java)
                .putExtra(DownloadService.MEDIA_OBJECT_IDS, item.mediaObjects.map { it.id }.toLongArray())
                .putExtra(DownloadService.PENDING_INTENT,pi )
        startService(intent)

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