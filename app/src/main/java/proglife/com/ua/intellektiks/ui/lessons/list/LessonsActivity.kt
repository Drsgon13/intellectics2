package proglife.com.ua.intellektiks.ui.lessons.list

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_lessons.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.data.models.LessonPreview
import proglife.com.ua.intellektiks.ui.base.BaseActivity
import proglife.com.ua.intellektiks.ui.lessons.show.LessonActivity

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class LessonsActivity: BaseActivity(), LessonsView {

    @InjectPresenter
    lateinit var mPresenter: LessonsPresenter

    private lateinit var mAdapter: LessonsAdapter

    @ProvidePresenter
    fun providePresenter(): LessonsPresenter {
        return LessonsPresenter(intent.getParcelableExtra(Constants.Field.GOODS_PREVIEW))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_lessons)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setTitle(R.string.user_goods)

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)
        mAdapter = LessonsAdapter(mPresenter)
        rvLessons.layoutManager = LinearLayoutManager(this)
        rvLessons.addItemDecoration(divider)
        rvLessons.adapter = mAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        withBackAnimation()
    }

    override fun showInfo(goodsPreview: GoodsPreview) {
        tvTitle.text = goodsPreview.name
    }

    override fun showLoading() {
        pbLoading.show()
    }

    override fun dismissLoading() {
        pbLoading.hide()
    }

    override fun showLessons(list: List<LessonPreview>) {
        mAdapter.show(list)
    }

    override fun showLesson(lessonPreview: LessonPreview) {
        startActivity(Intent(this, LessonActivity::class.java)
                .putExtra(Constants.Field.LESSONS_PREVIEW, lessonPreview))
        withStartAnimation()
    }
}