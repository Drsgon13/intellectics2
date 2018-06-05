package proglife.com.ua.intellektiks.ui.favorites

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_favorites.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.Favorite
import proglife.com.ua.intellektiks.ui.base.BaseActivity

class FavoritesActivity: BaseActivity(), FavoritesView {

    @InjectPresenter
    lateinit var presenter: FavoritesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_favorites)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setTitle(R.string.nav_favorites)
        presenter.loadFavorites()
    }

    override fun showFavorites(it: List<Favorite>) {
       rvFavorites.adapter = FavoritesAdapter(it)
    }

}