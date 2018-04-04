package proglife.com.ua.intellektiks.data.repositories

import io.reactivex.Observable
import io.reactivex.Single
import proglife.com.ua.intellektiks.data.models.*
import proglife.com.ua.intellektiks.data.network.apis.CommonApi
import proglife.com.ua.intellektiks.data.network.models.*

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class NetworkRepository(private val commonApi: CommonApi) {

    fun getUserData(login: String, password: String): Single<UserData> {
        return commonApi.getUserData(GetUserDataRequest(login, password))
    }

    fun getUserGoods(login: String, password: String): Observable<List<GoodsPreview>> {
        return commonApi.getUserGoods(GetUserGoodsRequest(login, password))
    }

    fun getGoods(login: String, password: String, id: Long): Observable<Goods> {
        return commonApi.getGoods(GetGoodsRequest(login, password, id))
    }

    fun getLessons(login: String, password: String, id: Long): Observable<List<LessonPreview>> {
        return commonApi.getLessons(GetLessonsRequest(login, password, id))
    }

    fun getLesson(login: String, password: String, id: Long): Observable<Lesson> {
        return commonApi.getLesson(GetLessonRequest(login, password, id))
    }

    fun getHelp() : Single<Help> = commonApi.getHelp()
}