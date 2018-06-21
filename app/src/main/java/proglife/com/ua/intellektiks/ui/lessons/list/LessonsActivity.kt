package proglife.com.ua.intellektiks.ui.lessons.list

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_lessons.*
import kotlinx.android.synthetic.main.content_main.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.data.models.LessonPreview
import proglife.com.ua.intellektiks.ui.base.BaseActivity
import proglife.com.ua.intellektiks.ui.content.ContentActivity
import proglife.com.ua.intellektiks.ui.lessons.show.LessonActivity

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class LessonsActivity: BaseActivity(), LessonsView {

    @InjectPresenter
    lateinit var mPresenter: LessonsPresenter

    private lateinit var mAdapter: LessonsAdapter
    private var isFavorite: Boolean = false

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        if( intent.getParcelableExtra(Constants.Field.GOODS_PREVIEW) as GoodsPreview? !=null ) {
            menuInflater.inflate(R.menu.favorites, menu)
            menu.findItem(R.id.action_favorite).isChecked = isFavorite
            setStateMenuItem(menu.findItem(R.id.action_favorite))
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.isChecked = !item.isChecked
        setStateMenuItem( item)
        mPresenter.favorite(item.isChecked)
        return super.onOptionsItemSelected(item)
    }

    fun setStateMenuItem(item: MenuItem){
        if(item.isChecked){
            item.icon = ContextCompat.getDrawable(this, R.drawable.ic_star)
        } else {
            item.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_border)
        }
    }


    override fun favoriteState(favorite: Boolean) {
        isFavorite = favorite
        invalidateOptionsMenu()

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
        startActivity(Intent(this, ContentActivity::class.java)
                .putExtra(Constants.Field.LESSONS_PREVIEW, lessonPreview))
        withStartAnimation()
    }

    override fun showNoData() {
        Snackbar.make(coordinator, R.string.error_network, Snackbar.LENGTH_LONG).show()
    }

    override fun showError(message: String) {

    }

    override fun showError(res: Int) {

    }
}