package proglife.com.ua.intellektiks.ui.favorites

import proglife.com.ua.intellektiks.data.models.Favorite
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.ui.base.BaseView

interface FavoritesView: BaseView {
    fun showFavorites(it: List<Favorite>)
    fun removeItem(favorite: Favorite)
    fun showConfirmRemove(favorite: Favorite)
    fun openGoods(goodsPreview: GoodsPreview)
    fun showError(res: Int)
}