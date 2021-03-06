package proglife.com.ua.intellektiks.data.repositories

import io.reactivex.Observable
import io.reactivex.Single
import org.json.JSONException
import proglife.com.ua.intellektiks.data.models.*
import proglife.com.ua.intellektiks.data.network.apis.CommonApi
import proglife.com.ua.intellektiks.data.network.models.*
import java.io.EOFException

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

    fun getHelp(): Observable<Help> = commonApi.getHelp()

    fun getNotifications(login: String, password: String): Observable<List<NotificationMessagePreview>> {
        return commonApi.getNotifications(GetNotificationsRequest(login, password))
    }

    fun getNotification(login: String, password: String, id: Long): Observable<NotificationMessage> {
        return commonApi.getNotification(GetNotificationRequest(login, password, id))
    }

    fun updateNotification(login: String, password: String, id: Long): Observable<Unit> {
        return commonApi.updateNotification(UpdateNotificationsRequest(login, password, id))
    }

    fun getNotificationUrl(id: Long): Observable<NotificationURL> {
        return commonApi.getNotificationUrl(GetNotificationURLRequest(id))
    }

    fun createLessonMessage(login: String, password: String, userId: Long, lessonId: Long, message: String): Single<CreateLessonMessageResponse> {
        return commonApi.createLessonMessage(CreateLessonMessageRequest(login, password, userId, lessonId, message))
    }

    fun createReminder(login: String, password: String, contactId: Long, goodsId: Long?, lessonId: Long?, seconds: Long, mediaObjectId: Long): Observable<ReminderResponse> {
        return commonApi.createReminder(CreateReminderRequest(login, password, contactId, goodsId, lessonId, seconds, mediaObjectId))
    }

    fun deleteReminder(login: String, password: String, contactId: Long, goodsId: Long?, lessonId: Long?, mediaObjectId: Long): Observable<Unit> {
        return commonApi.deleteReminder(DeleteReminderRequest(login, password, contactId, goodsId, lessonId, mediaObjectId))
                .onErrorReturn {
                    if (it is JSONException) return@onErrorReturn
                    throw Exception(it)
                }
    }

    fun getFavorites(login: String, password: String): Observable<List<Favorite>> {
        return commonApi.getFavorites(GetFavoritesRequest(login, password))
    }
    fun changeFavorite(login: String, password: String, action: String, id: String?, id_bookmark: String?): Observable<ResponseFavorite> {
        return commonApi.changeFavorite(SetFavoritesRequest(login, password, action, id, id_bookmark))
    }

    fun subsFcm(deviceId: String, contactId: Long, token: String): Single<Unit> {
        return commonApi.subsFcm(SubsFcmRequest(deviceId, contactId, token))
                .map { Unit }
    }

    fun unSubsFcm(deviceId: String): Single<Unit> {
        return commonApi.unSubsFcm(UnSubsFcmRequest(deviceId))
                .map { Unit }
    }

    fun unreadNotifications(deviceId: String, login: String, pass: String): Single<Int> {
        return commonApi.unreadNotifications(UnReadNotificationsRequest(deviceId, login, pass))
                .map{t -> t.count }

    }

    fun recoveryPassword(email: String): Single<Unit> {
        return commonApi.recoveryPassword(RecoveryPasswordRequest(email))
    }

    fun getCards(login: String, password: String): Single<List<Card>> {
        return commonApi.getCards(GetCardsRequest(login, password))
    }

    fun removeCard(login: String, password: String, id: Long): Single<String> {
        return commonApi.removeCard(RemoveCardRequest(login, password, id))
                .map { it.message }
    }

    fun callPayment(login: String, password: String, offerId: Long, action: Int): Single<CallPaymentResponse> {
        return commonApi.callPayment(CallPaymentRequest(login, password, offerId, action))
    }

}