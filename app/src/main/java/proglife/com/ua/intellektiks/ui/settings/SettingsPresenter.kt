package proglife.com.ua.intellektiks.ui.settings

import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.Card
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class SettingsPresenter: BasePresenter<SettingsView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    init {
        injector().inject(this)
        mCommonInteractor.userData()
                .compose(oAsync())
                .subscribe(
                        {
                            viewState.showData(it)
                        },
                        {

                        }
                )
        mCommonInteractor.getMediaCacheSize()
                .compose(sAsync())
                .subscribe(
                        {
                            viewState.showCacheSize(it)
                        },
                        {}
                )

        mCommonInteractor.getCards()
                .compose(sAsync())
                .doOnSubscribe { viewState.showCardsLoading() }
                .doFinally { viewState.dismissCardsLoading() }
                .subscribe(
                        {
                            viewState.showCards(it)
                        },
                        {
                            viewState.showCardsError(it.message)
                        }
                )
    }

    fun clearCache() {
        mCommonInteractor.clearMediaCache()
                .flatMap { mCommonInteractor.getMediaCacheSize() }
                .compose(sAsync())
                .doOnSubscribe { viewState.showClearCacheLoading() }
                .doOnEvent { _, _ -> viewState.dismissClearCacheLoading() }
                .subscribe(
                        {
                            viewState.showCacheSize(it)
                        },
                        {

                        }
                )
    }

    fun removeCard(card: Card) {
        mCommonInteractor.removeCard(card)
                .compose(sAsync())
                .doOnSubscribe { viewState.showRemoveCardLoading() }
                .doFinally { viewState.dismissRemoveCardLoading() }
                .subscribe(
                        {
                            viewState.showCards(emptyList())
                        },
                        {
                            viewState.showError(R.string.cards_remove_error)
                        }
                )
    }

}