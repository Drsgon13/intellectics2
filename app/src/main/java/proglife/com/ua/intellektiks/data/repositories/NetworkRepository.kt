package proglife.com.ua.intellektiks.data.repositories

import io.reactivex.Observable
import io.reactivex.Single
import org.json.JSONException
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

    fun getHelp(): Observable<Help> = commonApi.getHelp()

    fun getNotifications(login: String, password: String): Observable<List<NotificationMessagePreview>> {
        return commonApi.getNotifications(GetNotificationsRequest(login, password))
    }

    fun getNotification(login: String, password: String, id: Long): Observable<NotificationMessage> {
        return commonApi.getNotification(GetNotificationRequest(login, password, id))
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

    fun subsFcm(deviceId: String, contactId: Long, token: String): Single<Unit> {
        return commonApi.subsFcm(SubsFcmRequest(deviceId, contactId, token))
                .map { Unit }
    }

    fun unSubsFcm(deviceId: String): Single<Unit> {
        return commonApi.unSubsFcm(UnSubsFcmRequest(deviceId))
                .map { Unit }
    }

    fun unreadNotifications(deviceId: String): Single<Int> {
        return commonApi.unreadNotifications(UnReadNotificationsRequest(deviceId))
                // TODO
                .map { 0 }
    }

}