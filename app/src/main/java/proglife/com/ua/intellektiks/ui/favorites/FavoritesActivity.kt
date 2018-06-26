package proglife.com.ua.intellektiks.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.content_main.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.data.models.Favorite
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.ui.base.BaseActivity
import proglife.com.ua.intellektiks.ui.content.ContentActivity
import proglife.com.ua.intellektiks.ui.lessons.list.LessonsActivity
import proglife.com.ua.intellektiks.ui.lessons.show.LessonActivity

class FavoritesActivity : BaseActivity(), FavoritesView {

    @InjectPresenter
    lateinit var presenter: FavoritesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_favorites)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setTitle(R.string.nav_favorites)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadFavorites()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        withBackAnimation()
    }

    override fun showFavorites(it: List<Favorite>) {
        emptyContainer.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        rvFavorites.adapter = FavoritesAdapter(it.toMutableList(), presenter)
    }

    override fun removeItem(favorite: Favorite) {
        (rvFavorites.adapter as FavoritesAdapter).removeItem(favorite)
    }

    override fun openGoods(goodsPreview: GoodsPreview) {
        startActivity(Intent(this, ContentActivity::class.java)
                .putExtra(Constants.Field.GOODS_PREVIEW, goodsPreview))
        withStartAnimation()
    }

    override fun openLessons(goodsPreview: GoodsPreview) {
        startActivity(Intent(this, LessonsActivity::class.java)
                .putExtra(Constants.Field.GOODS_PREVIEW, goodsPreview))
        withStartAnimation()
    }

    override fun showConfirmRemove(favorite: Favorite) {
        AlertDialog.Builder(this)
                .setMessage(getString(R.string.dialog_delete_favorite))
                .setPositiveButton(getString(R.string.delete)) { _, _ -> presenter.delete(favorite) }
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
                .show()
    }

    override fun showError(res: Int) {
        Snackbar.make(coordinator, res, Snackbar.LENGTH_LONG).show()
    }
}