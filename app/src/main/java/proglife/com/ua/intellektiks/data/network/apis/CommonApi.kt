package proglife.com.ua.intellektiks.data.network.apis

import io.reactivex.Single
import proglife.com.ua.intellektiks.data.models.*
import proglife.com.ua.intellektiks.data.network.models.*
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface CommonApi {

    companion object {
        const val JSON = "?r=api/apps/json"
    }

    @POST(JSON)
    fun getUserData(
            @Body body: GetUserDataRequest
    ): Single<UserData>

    @POST(JSON)
    fun getUserGoods(
            @Body body: GetUserGoodsRequest
    ): Single<List<GoodsPreview>>

    @POST(JSON)
    fun getGoods(
            @Body body: GetGoodsRequest
    ): Single<Goods>

    @POST(JSON)
    fun getLessons(
            @Body body: GetLessonsRequest
    ): Single<List<LessonPreview>>

    @POST(JSON)
    fun getLesson(
            @Body body: GetLessonRequest
    ): Single<Lesson>

}