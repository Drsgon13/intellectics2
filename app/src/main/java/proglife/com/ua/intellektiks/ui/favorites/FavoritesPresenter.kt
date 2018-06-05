package proglife.com.ua.intellektiks.ui.favorites

import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.ui.base.BasePresenter
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
                            it.printStackTrace()
                        }
                )
    }
}