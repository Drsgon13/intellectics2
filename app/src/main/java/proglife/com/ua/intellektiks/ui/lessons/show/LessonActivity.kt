package proglife.com.ua.intellektiks.ui.lessons.show

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_goods_show_alt.*
import kotlinx.android.synthetic.main.content_main.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.data.models.Lesson
import proglife.com.ua.intellektiks.data.models.LessonPreview
import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.ui.base.BaseActivity
import proglife.com.ua.intellektiks.ui.base.media.MediaObjectAdapter
import proglife.com.ua.intellektiks.utils.PositionListener

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class LessonActivity: BaseActivity(), LessonView, PositionListener {

    private lateinit var mMediaObjectAdapter: MediaObjectAdapter

    @InjectPresenter
    lateinit var mPresenter: LessonPresenter

    @ProvidePresenter
    fun providePresenter(): LessonPresenter {
        return LessonPresenter(intent.getParcelableExtra(Constants.Field.LESSONS_PREVIEW))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_lesson)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setTitle(R.string.user_goods)

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)
        mMediaObjectAdapter = MediaObjectAdapter(object : MediaObjectAdapter.OnSelectMediaObjectListener {
            override fun onDownload(mediaObject: MediaObject) {
            }
            override fun onSelect(mediaObject: MediaObject) {
            }
        })
        rvMediaObjects.layoutManager = LinearLayoutManager(this)
        rvMediaObjects.addItemDecoration(divider)
        rvMediaObjects.adapter = mMediaObjectAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        withBackAnimation()
    }

    override fun showInfo(lessonPreview: LessonPreview) {
        tvName.text = lessonPreview.name
    }

    override fun showLoading() {
        pbLoading.show()
    }

    override fun dismissLoading() {
        pbLoading.hide()
    }

    override fun showLesson(lesson: Lesson) {
        mMediaObjectAdapter.show(lesson.getMediaObjects())
    }

    override fun showNoData() {
        Snackbar.make(coordinator, R.string.error_network, Snackbar.LENGTH_LONG).show()
    }

    override fun onClickPosition(position: Int) {

    }
}