package proglife.com.ua.intellektiks

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import proglife.com.ua.intellektiks.di.application.ApplicationComponent
import proglife.com.ua.intellektiks.di.application.DaggerApplicationComponent
import proglife.com.ua.intellektiks.di.application.modules.AppModule
import io.reactivex.plugins.RxJavaPlugins



/**
 * Created by Evhenyi Shcherbyna on 22.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */

class App: Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmStatic lateinit var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()

        RxJavaPlugins.setErrorHandler { e ->
            Log.w("RxJavaPlugins","Undeliverable exception received", e)
        }
    }

    private fun initDagger() {
        graph = DaggerApplicationComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

}
