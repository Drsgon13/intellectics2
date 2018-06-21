package proglife.com.ua.intellektiks.ui.lessons.list

import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.data.models.LessonPreview
import proglife.com.ua.intellektiks.data.network.ServerException
import proglife.com.ua.intellektiks.data.network.models.SetFavoritesRequest
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class LessonsPresenter(private val goodsPreview: GoodsPreview): BasePresenter<LessonsView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    private var mList: List<LessonPreview> = emptyList()

    init {
        injector().inject(this)
        viewState.favoriteState(!(goodsPreview.idFavorite!!.isBlank() || goodsPreview.idFavorite!! == "0"))
        viewState.showInfo(goodsPreview)
        goodsPreview.trainingId?.let {
            mCommonInteractor.getLessons(it)
                    .compose(oAsync())
                    .doOnSubscribe { viewState.showLoading() }
                    .doOnNext { viewState.dismissLoading() }
                    .subscribe(
                            {
                                mList = it
                                viewState.showLessons(it)
                            },
                            {
                                if (it is IOException && mList.isEmpty()) {
                                    viewState.showNoData()
                                }
                            }
                    )
        }
    }

    fun favorite(boolean: Boolean){
        var goods :String? = null
        var bookmark :String? = null

        if(boolean)
            goods = goodsPreview!!.id.toString()
        else bookmark = goodsPreview!!.idFavorite
        if(bookmark == "0" && !boolean) return
        mCommonInteractor.changeFavorite(if(boolean) SetFavoritesRequest.ADD else SetFavoritesRequest.DELETE, goods, bookmark)
                .compose(oAsync())
                .subscribe(
                        {
                            if(boolean)
                                goodsPreview.idFavorite = it.id.toString()
                            else goodsPreview.idFavorite = "0"
                            viewState.favoriteState(boolean)

                        },
                        {
                            if(it is ServerException && it.message!=null)
                                viewState.showError(it.message!!)
                            if(it is UnknownHostException)
                                viewState.showError(R.string.error_network)

                            viewState.favoriteState(!boolean)
                            it.printStackTrace()
                        }
                )
    }

    fun openLesson(lessonPreview: LessonPreview) {
        if (lessonPreview.access) viewState.showLesson(lessonPreview)
    }

}