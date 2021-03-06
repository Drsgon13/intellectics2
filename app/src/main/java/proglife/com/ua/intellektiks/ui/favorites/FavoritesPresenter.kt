package proglife.com.ua.intellektiks.ui.favorites

import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.Favorite
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.data.network.models.SetFavoritesRequest
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

@InjectViewState
class FavoritesPresenter : BasePresenter<FavoritesView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    init {
        injector().inject(this)
    }

    fun loadFavorites() {
        mCommonInteractor.getFavorites()
                .compose(oAsync())
                .subscribe(
                        {
                            viewState.showFavorites(it)
                        },
                        {
                            if(it is UnknownHostException)
                                viewState.showError(R.string.error_network)
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
                            if(it is UnknownHostException)
                                viewState.showError(R.string.error_network)
                            it.printStackTrace()
                        }
                )
    }

    fun openItem(favorite: Favorite) {
        if(favorite.idTraining!=null)
            viewState.openLessons(GoodsPreview(favorite.idGoods.toLong(), favorite.name, Date(), "", favorite.idTraining, favorite.id))
        else viewState.openGoods(GoodsPreview(favorite.idGoods.toLong(), favorite.name, Date(), "", null, favorite.id))
    }
}