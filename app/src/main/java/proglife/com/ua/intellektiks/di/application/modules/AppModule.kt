package proglife.com.ua.intellektiks.di.application.modules

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import proglife.com.ua.intellektiks.App
import proglife.com.ua.intellektiks.data.network.apis.CommonApi
import proglife.com.ua.intellektiks.data.repositories.NetworkRepository
import proglife.com.ua.intellektiks.data.repositories.SPRepository
import proglife.com.ua.intellektiks.utils.AESCrypt

/**
 * Created by Evhenyi Shcherbyna on 23.10.2017.
 * Copyright (c) 2017 ProgLife. All rights reserved.
 */
@Module
class AppModule(private val application: App) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return this.application
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()
    }

    @Provides
    @Singleton
    fun provideSPRepository(context: Context, gson: Gson, aesCrypt: AESCrypt): SPRepository {
        return SPRepository(context, gson, aesCrypt)
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(commonApi: CommonApi): NetworkRepository {
        return NetworkRepository(commonApi)
    }

    @Provides
    @Singleton
    fun provideAESCrypt(): AESCrypt {
        return AESCrypt()
    }

}
