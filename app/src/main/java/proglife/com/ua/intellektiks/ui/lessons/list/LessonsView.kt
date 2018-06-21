package proglife.com.ua.intellektiks.ui.lessons.list

import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.data.models.LessonPreview
import proglife.com.ua.intellektiks.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface LessonsView: BaseView {
    fun showInfo(goodsPreview: GoodsPreview)
    fun showLoading()
    fun dismissLoading()
    fun showLessons(list: List<LessonPreview>)
    fun showLesson(lessonPreview: LessonPreview)
    fun showNoData()
    fun favoriteState(favorite: Boolean)
    fun showError(message: String)
    fun showError(res: Int)
}