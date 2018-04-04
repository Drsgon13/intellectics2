package proglife.com.ua.intellektiks.ui.lessons.show

import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.Lesson
import proglife.com.ua.intellektiks.data.models.LessonPreview
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class LessonPresenter(private val lessonPreview: LessonPreview): BasePresenter<LessonView>() {

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    private var mLesson: Lesson? = null

    init {
        injector().inject(this)
        viewState.showInfo(lessonPreview)
        mCommonInteractor.getLesson(lessonPreview.id)
                .compose(oAsync())
                .doOnSubscribe { viewState.showLoading() }
                .doOnNext { viewState.dismissLoading() }
                .subscribe(
                        {
                            mLesson = it
                            viewState.showLesson(it)
                        },
                        {
                            if (it is IOException && mLesson == null) {
                                viewState.showNoData()
                            }
                        }
                )
    }

}