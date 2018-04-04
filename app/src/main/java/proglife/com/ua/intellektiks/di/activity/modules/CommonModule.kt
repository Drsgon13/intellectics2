package proglife.com.ua.intellektiks.di.activity.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.repositories.NetworkRepository
import proglife.com.ua.intellektiks.data.repositories.SPRepository
import proglife.com.ua.intellektiks.di.activity.ActivityScope
import proglife.com.ua.intellektiks.utils.AESCrypt

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@Module
class CommonModule {

    @ActivityScope
    @Provides
    fun provideCommonInteractor(
            context: Context,
//            dbRepository: DBRepository,
            spRepository: SPRepository,
            networkRepository: NetworkRepository,
            aesCrypt: AESCrypt
    ): CommonInteractor {
        return CommonInteractor(context, spRepository, networkRepository)
    }



}