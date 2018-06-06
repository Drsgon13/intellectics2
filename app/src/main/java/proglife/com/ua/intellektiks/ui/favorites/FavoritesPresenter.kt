package proglife.com.ua.intellektiks.ui.favorites

import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.Favorite
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.data.network.models.SetFavoritesRequest
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import java.util.*
import javax.inject.Inject

@InjectViewState
class FavoritesPresenter : BasePresenter<FavoritesView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    init {
        injector().inject(this)
    }

    fun loadCash(){
        mCommonInteractor.getFavoritesCash()
                .compose(sAsync())
                .subscribe(
                        {
                            viewState.showFavorites(it)
                            loadFavorites()
                        },
                        {
                            it.printStackTrace()
                        }
                )
    }

    fun loadFavorites() {
        mCommonInteractor.getFavorites()
                .compose(oAsync())
                .subscribe(
                        {
                            viewState.showFavorites(it)
                        },
                        {
                            it.printStackTrace()
                        }
                )
    }

    fun showConfirm(favorite: Favorite){
        viewState.showConfirmRemove(favorite)
    }

    fun delete(favorite: Favorite){
        mCommonInteractor.changeFavorite(SetFavoritesRequest.DELETE, null, favorite.id)
                .compose(oAsync())
                .subscribe(
                        {
                            viewState.removeItem(favorite)
                        },
                        {
                            viewState.showError(R.string.error_network)
                            it.printStackTrace()
                        }
                )
    }

    fun openItem(favorite: Favorite) {
        viewState.openGoods(GoodsPreview(favorite.idGoods.toLong(), favorite.name, Date(), "", null))
    }
}