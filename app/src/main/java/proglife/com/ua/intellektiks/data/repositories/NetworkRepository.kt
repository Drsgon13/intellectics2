package proglife.com.ua.intellektiks.data.repositories

import io.reactivex.Single
import proglife.com.ua.intellektiks.data.models.Goods
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.data.models.UserData
import proglife.com.ua.intellektiks.data.network.apis.CommonApi
import proglife.com.ua.intellektiks.data.network.models.GetGoodsRequest
import proglife.com.ua.intellektiks.data.network.models.GetUserDataRequest
import proglife.com.ua.intellektiks.data.network.models.GetUserGoodsRequest

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class NetworkRepository(private val commonApi: CommonApi) {

    fun getUserData(login: String, password: String): Single<UserData> {
        return commonApi.getUserData(GetUserDataRequest(login, password))
    }

    fun getUserGoods(login: String, password: String): Single<List<GoodsPreview>> {
        return commonApi.getUserGoods(GetUserGoodsRequest(login, password))
    }

    fun getGoods(login: String, password: String, id: Long): Single<Goods> {
        return commonApi.getGoods(GetGoodsRequest(login, password, id))
    }

}