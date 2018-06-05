package proglife.com.ua.intellektiks.ui.favorites

import proglife.com.ua.intellektiks.data.models.Favorite
import proglife.com.ua.intellektiks.ui.base.BaseView

interface FavoritesView: BaseView {
    fun showFavorites(it: List<Favorite>)
}