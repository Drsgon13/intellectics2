package proglife.com.ua.intellektiks.data.network.apis

import io.reactivex.Observable
import io.reactivex.Single
import proglife.com.ua.intellektiks.data.models.*
import proglife.com.ua.intellektiks.data.network.models.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface CommonApi {

    companion object {
        const val JSON = "?r=api/apps/json"
        const val HELP = "?r=api/apps/help"
        const val REMINDER = "?r=api/apps/xdkreminder"
    }

    @POST(JSON)
    fun getUserData(
            @Body body: GetUserDataRequest
    ): Single<UserData>

    @POST(JSON)
    fun getUserGoods(
            @Body body: GetUserGoodsRequest
    ): Observable<List<GoodsPreview>>

    @POST(JSON)
    fun getGoods(
            @Body body: GetGoodsRequest
    ): Observable<Goods>

    @POST(JSON)
    fun getLessons(
            @Body body: GetLessonsRequest
    ): Observable<List<LessonPreview>>

    @POST(JSON)
    fun getLesson(
            @Body body: GetLessonRequest
    ): Observable<Lesson>

    @GET(HELP)
    fun getHelp(): Observable<Help>

    @POST(JSON)
    fun getNotifications(
            @Body body: GetNotificationsRequest
    ): Observable<List<NotificationMessagePreview>>

    @POST(JSON)
    fun getNotification(
            @Body body: GetNotificationRequest
    ): Observable<NotificationMessage>

    @POST(JSON)
    fun createLessonMessage(
            @Body request: CreateLessonMessageRequest
    ): Observable<Unit>

    @POST(REMINDER)
    fun createReminder(
            @Body request: CreateReminderRequest
    ): Observable<ReminderResponse>

}