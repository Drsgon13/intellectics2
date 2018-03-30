package proglife.com.ua.intellektiks.di.activity

import dagger.Subcomponent
import proglife.com.ua.intellektiks.di.activity.modules.CommonModule
import proglife.com.ua.intellektiks.extensions.DownloadService
import proglife.com.ua.intellektiks.ui.auth.AuthPresenter
import proglife.com.ua.intellektiks.ui.common.CommonPresenter
import proglife.com.ua.intellektiks.ui.goods.GoodsShowPresenter
import proglife.com.ua.intellektiks.ui.lessons.list.LessonsPresenter
import proglife.com.ua.intellektiks.ui.lessons.show.LessonPresenter
import proglife.com.ua.intellektiks.ui.main.MainPresenter
import proglife.com.ua.intellektiks.ui.profile.ProfilePresenter
import proglife.com.ua.intellektiks.ui.splash.SplashPresenter
import proglife.com.ua.intellektiks.ui.support.SupportPresenter

/**
 * Created by Evhenyi Shcherbyna on 23.10.2017.
 * Copyright (c) 2017 ProgLife. All rights reserved.
 */
@ActivityScope
@Subcomponent(modules = [CommonModule::class])
interface ActivityComponent {
    fun inject(presenter: AuthPresenter)
    fun inject(presenter: MainPresenter)
    fun inject(presenter: SplashPresenter)
    fun inject(presenter: CommonPresenter)
    fun inject(presenter: ProfilePresenter)
    fun inject(presenter: SupportPresenter)
    fun inject(presenter: GoodsShowPresenter)
    fun inject(presenter: LessonsPresenter)
    fun inject(presenter: LessonPresenter)
    fun inject(service: DownloadService)
}
