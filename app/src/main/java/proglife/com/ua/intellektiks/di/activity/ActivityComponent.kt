package proglife.com.ua.intellektiks.di.activity

import dagger.Subcomponent
import proglife.com.ua.intellektiks.di.activity.modules.CommonModule
import proglife.com.ua.intellektiks.extensions.DownloadService
import proglife.com.ua.intellektiks.ui.auth.AuthPresenter
import proglife.com.ua.intellektiks.ui.common.CommonPresenter
import proglife.com.ua.intellektiks.ui.content.ContentPresenter
import proglife.com.ua.intellektiks.ui.favorites.FavoritesPresenter
import proglife.com.ua.intellektiks.ui.goods.GoodsShowPresenter
import proglife.com.ua.intellektiks.ui.lessons.list.LessonsPresenter
import proglife.com.ua.intellektiks.ui.lessons.show.LessonPresenter
import proglife.com.ua.intellektiks.ui.main.MainPresenter
import proglife.com.ua.intellektiks.ui.notifications.list.NotificationListPresenter
import proglife.com.ua.intellektiks.ui.notifications.show.NotificationShowPresenter
import proglife.com.ua.intellektiks.ui.remember.ForgotPresenter
import proglife.com.ua.intellektiks.ui.settings.SettingsPresenter
import proglife.com.ua.intellektiks.ui.splash.SplashPresenter
import proglife.com.ua.intellektiks.ui.support.SupportPresenter
import proglife.com.ua.intellektiks.ui.viewer.media.ViewerMediaPresenter

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
    fun inject(presenter: SettingsPresenter)
    fun inject(presenter: SupportPresenter)
    fun inject(presenter: GoodsShowPresenter)
    fun inject(presenter: LessonsPresenter)
    fun inject(presenter: LessonPresenter)
    fun inject(service: DownloadService)
    fun inject(presenter: NotificationListPresenter)
    fun inject(presenter: NotificationShowPresenter)
    fun inject(presenter: ViewerMediaPresenter)
    fun inject(presenter: ContentPresenter)
    fun inject(presenter: ForgotPresenter)
    fun inject(presenter: FavoritesPresenter)
}
