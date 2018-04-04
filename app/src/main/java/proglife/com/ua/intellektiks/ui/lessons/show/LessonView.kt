package proglife.com.ua.intellektiks.ui.lessons.show

import proglife.com.ua.intellektiks.data.models.Lesson
import proglife.com.ua.intellektiks.data.models.LessonPreview
import proglife.com.ua.intellektiks.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface LessonView: BaseView {
    fun showInfo(lessonPreview: LessonPreview)
    fun showLoading()
    fun dismissLoading()
    fun showLesson(lesson: Lesson)
    fun showNoData()
}