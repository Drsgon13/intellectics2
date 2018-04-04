package proglife.com.ua.intellektiks.ui.lessons.list

import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.data.models.LessonPreview
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class LessonsPresenter(goodsPreview: GoodsPreview): BasePresenter<LessonsView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    private var mList: List<LessonPreview> = emptyList()

    init {
        injector().inject(this)
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

    fun openLesson(lessonPreview: LessonPreview) {
        viewState.showLesson(lessonPreview)
    }

}