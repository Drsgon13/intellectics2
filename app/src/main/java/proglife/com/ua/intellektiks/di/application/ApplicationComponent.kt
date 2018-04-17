package proglife.com.ua.intellektiks.di.application

import com.google.gson.Gson
import javax.inject.Singleton

import dagger.Component
import proglife.com.ua.intellektiks.di.activity.ActivityComponent
import proglife.com.ua.intellektiks.di.application.modules.AppModule
import proglife.com.ua.intellektiks.di.application.modules.NetworkModule

/**
 * Created by Evhenyi Shcherbyna on 23.10.2017.
 * Copyright (c) 2017 ProgLife. All rights reserved.
 */
@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface ApplicationComponent {

    fun provideGson(): Gson
    fun activityComponent(): ActivityComponent
}
