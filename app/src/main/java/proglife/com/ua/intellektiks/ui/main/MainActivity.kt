package proglife.com.ua.intellektiks.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.ui.base.NavBaseActivity
import proglife.com.ua.intellektiks.ui.content.ContentActivity
import proglife.com.ua.intellektiks.ui.lessons.list.LessonsActivity

class MainActivity : NavBaseActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private lateinit var mAdapter: GoodsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_main)

        supportActionBar?.setTitle(R.string.user_goods)

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)
        mAdapter = GoodsAdapter(presenter)
        mAdapter.setHasStableIds(true)
        rvGoods.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvGoods.addItemDecoration(divider)
        rvGoods.adapter = mAdapter
    }

    override fun onResume() {
        super.onResume()
        if(rvGoods.childCount > 0)
            presenter.loadGoods()
    }

    override fun showLoading() {
        pbLoading.show()
    }

    override fun dismissLoading() {
        pbLoading.hide()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == Constants.REQUEST_RESULT && resultCode == Activity.RESULT_OK)
            mAdapter.changeItem(data!!.getParcelableExtra(Constants.Field.GOODS_PREVIEW))
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showError(message: String?) {
        message?.let { Snackbar.make(coordinator, message, Snackbar.LENGTH_LONG).show() }
    }

    override fun showNeedAuth() {
        tvMessage.visibility = View.VISIBLE
    }

    override fun showGoods(list: List<GoodsPreview>) {
        tvMessage.visibility = View.GONE
        mAdapter.show(list)
    }

    override fun showGoods(item: GoodsPreview) {
        startActivityForResult(Intent(this, ContentActivity::class.java)
                .putExtra(Constants.Field.GOODS_PREVIEW, item), Constants.REQUEST_RESULT)
        withStartAnimation()
    }

    override fun showLessons(item: GoodsPreview) {
        startActivity(Intent(this, LessonsActivity::class.java)
                .putExtra(Constants.Field.GOODS_PREVIEW, item))
        withStartAnimation()
    }

    override fun showNoData() {
        Snackbar.make(coordinator, R.string.error_network, Snackbar.LENGTH_LONG).show()
    }

    override fun showRateRequest() {
        RateDialog().apply {
            action = presenter
        }.show(supportFragmentManager, "RateDialog")
    }
}
