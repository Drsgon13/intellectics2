package proglife.com.ua.intellektiks.data.network.apis

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
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

    @POST(JSON)
    fun getCards(
            @Body body: GetCardsRequest
    ): Single<List<Card>>

    @POST(JSON)
    fun removeCard(
            @Body body: RemoveCardRequest
    ): Single<RemoveCardResponse>

    @GET("?r=api/apps/help")
    fun getHelp(): Observable<Help>

    @POST(JSON)
    fun createLessonMessage(
            @Body request: CreateLessonMessageRequest
    ): Single<CreateLessonMessageResponse>

    @POST(JSON)
    fun callPayment(
            @Body request: CallPaymentRequest
    ): Single<CallPaymentResponse>

    @POST("?r=api/apps/xdkreminder")
    fun createReminder(
            @Body request: CreateReminderRequest
    ): Observable<ReminderResponse>

    @POST("?r=api/apps/xdkreminderdelete")
    fun deleteReminder(
            @Body request: DeleteReminderRequest
    ): Observable<Unit>

    // Подписка на push уведомления
    @POST("?r=api/push/xdkpushsubs")
    fun subsFcm(
            @Body request: SubsFcmRequest
    ): Single<ResponseBody>

    // Отписка от push уведомлений
    @POST("?r=api/push/xdkpushunsubs")
    fun unSubsFcm(
            @Body request: UnSubsFcmRequest
    ): Single<ResponseBody>

    // Количество непрочитанных сообщений
    @POST(JSON)
    fun unreadNotifications(
            @Body request: UnReadNotificationsRequest
    ): Single<UnReadNotificationsResponse>

    @POST(JSON)
    fun getNotifications(
            @Body body: GetNotificationsRequest
    ): Observable<List<NotificationMessagePreview>>

    @POST(JSON)
    fun getNotification(
            @Body body: GetNotificationRequest
    ): Observable<NotificationMessage>

    @POST(JSON)
    fun updateNotification(
            @Body body: UpdateNotificationsRequest
    ): Observable<Unit>

    @POST(JSON)
    fun getFavorites(
            @Body body: GetFavoritesRequest
    ): Observable<List<Favorite>>

    @POST(JSON)
    fun changeFavorite(
            @Body body: SetFavoritesRequest
    ): Observable<ResponseFavorite>

    @POST("?r=api/push/xdkgetpushdata")
    fun getNotificationUrl(
            @Body body: GetNotificationURLRequest
    ): Observable<NotificationURL>

    @POST("?r=api/apps/rememberpasswd")
    fun recoveryPassword(
            @Body body: RecoveryPasswordRequest
    ): Single<Unit>



}