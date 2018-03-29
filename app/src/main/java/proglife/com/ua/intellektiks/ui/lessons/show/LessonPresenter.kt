package proglife.com.ua.intellektiks.ui.lessons.show

import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.LessonPreview
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class LessonPresenter(private val lessonPreview: LessonPreview): BasePresenter<LessonView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    init {
        injector().inject(this)
        viewState.showInfo(lessonPreview)
        mCommonInteractor.getLesson(lessonPreview.id)
                .compose(sAsync())
                .doOnSubscribe { viewState.showLoading() }
                .doOnEvent { _, _ -> viewState.dismissLoading() }
                .subscribe(
                        {
                            viewState.showLesson(it)
                        },
                        {}
                )
    }

}