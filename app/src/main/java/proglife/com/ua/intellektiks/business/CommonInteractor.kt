package proglife.com.ua.intellektiks.business

import android.accounts.AuthenticatorException
import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import proglife.com.ua.intellektiks.data.models.*
import proglife.com.ua.intellektiks.data.network.models.CallPaymentResponse
import proglife.com.ua.intellektiks.data.network.models.GetNotificationURLRequest
import proglife.com.ua.intellektiks.data.network.models.ReminderResponse
import proglife.com.ua.intellektiks.data.repositories.NetworkRepository
import proglife.com.ua.intellektiks.data.repositories.SPRepository
import proglife.com.ua.intellektiks.extensions.DownloadableFile
import proglife.com.ua.intellektiks.utils.ExoUtils
import proglife.com.ua.intellektiks.utils.Hash
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class CommonInteractor(
        private val mContext: Context,
//        val mDbRepository: DBRepository,
        private val mSpRepository: SPRepository,
        private val mNetworkRepository: NetworkRepository
) {

    fun signIn(login: String, password: String, remember: Boolean): Single<UserData> {
        return mNetworkRepository.getUserData(login, password)
                .flatMap { data ->
                    if (remember)
                        subsFcm(data.id).map { data } else Single.just(data)
                }
                .doOnSuccess {
                    mSpRepository.credentials(Pair(login, password), remember)
                    mSpRepository.userData(it, remember)
                }
    }

    fun recoveryPassword(email: String): Single<Unit> {
        return mNetworkRepository.recoveryPassword(email)
    }

    fun logout(): Single<Unit> {
        return Single.fromCallable {
            unSubsFcm().subscribeOn(Schedulers.io()).subscribe({}, {})
            FirebaseInstanceId.getInstance().deleteInstanceId()
            mSpRepository.credentials(Pair(null, null), true)
            mSpRepository.userData(null, true)
        }
    }

    fun isAuthenticated(): Single<Boolean> {
        return Single.fromCallable {
            val credentials = mSpRepository.credentials()
            val userData = mSpRepository.userData()
            credentials.first != null && credentials.second != null && userData != null
        }
    }

    /**
     * Получение данных авторизации
     */
    private fun credentials(): Observable<Pair<String, String>> {
        return Observable.just(mSpRepository.credentials())
                .map {
                    if (it.first == null || it.second == null) {
                        throw AuthenticatorException()
                    }
                    Pair(it.first!!, it.second!!)
                }
    }

    /**
     * Получение главного списка "Мои товары"
     * Берем из SharedPreferences затем из сети
     */
    fun loadData(): Observable<List<GoodsPreview>> {
        return credentials()
                .flatMap {
                    Observable.mergeDelayError(
                            Observable.fromCallable { mSpRepository.userGoods() ?: emptyList() },
                            mNetworkRepository.getUserGoods(it.first, it.second)
                                    .doOnNext {
                                        mSpRepository.userGoods(it)
                                    }
                    )
                }
    }

    /**
     * Информация о пользователе
     */
    fun userData(): Observable<Pair<String?, UserData?>> {
        return Observable.fromCallable {
            Pair(mSpRepository.credentials().first, mSpRepository.userData())
        }
    }

    /**
     * Содержимое товара
     */
    fun getGoods(id: Long): Observable<Goods> {
        return credentials()
                .flatMap {
                    Observable.mergeDelayError(
                            Observable.fromCallable { mSpRepository.getGoods(id) ?: Unit },
                            mNetworkRepository.getGoods(it.first, it.second, id)
                                    .doOnNext {
                                        mSpRepository.setGoods(id, it)
                                    }
                    )
                }
                .filter { it is Goods }
                .cast(Goods::class.java)
    }

    fun getLessons(id: Long): Observable<List<LessonPreview>> {
        return credentials()
                .flatMap {
                    Observable.mergeDelayError(
                            Observable.fromCallable {
                                mSpRepository.getLessonPreviews(id) ?: emptyList()
                            },
                            mNetworkRepository.getLessons(it.first, it.second, id)
                                    .doOnNext {
                                        mSpRepository.setLessonPreviews(id, it)
                                    }
                    )
                }
    }

    fun getLesson(id: Long, ignoreCache: Boolean = false): Observable<Lesson> {
        return credentials()
                .flatMap {
                    if (ignoreCache) {
                        mNetworkRepository.getLesson(it.first, it.second, id)
                                .doOnNext {
                                    mSpRepository.setLesson(id, it)
                                }
                    } else {
                        Observable.mergeDelayError(
                                Observable.fromCallable { mSpRepository.getLesson(id) ?: Unit },
                                mNetworkRepository.getLesson(it.first, it.second, id)
                                        .doOnNext {
                                            mSpRepository.setLesson(id, it)
                                        }
                        )
                    }
                }
                .filter { it is Lesson }
                .cast(Lesson::class.java)
    }

    fun existsFiles(goods: Goods): Observable<Goods> {
        return Observable.just(goods)
                .map {
                    it.playerElements.forEach {
                        it.type = MediaObject.Type.PLAYER
                        val cExists = File("${mContext.filesDir}/c_${it.getFileName()}").exists()
                        val state = when {
                            cExists -> DownloadableFile.State.FINISHED
                            else -> DownloadableFile.State.NONE
                        }

                        it.downloadableFile = DownloadableFile.fromMediaObject(it, state)
                    }
                    it
                }
    }

    fun existsFiles(lesson: Lesson): Observable<Lesson> {
        return Observable.just(lesson)
                .map {
                    it.playerElements.forEach {
                        it.type = MediaObject.Type.PLAYER
                        val cExists = File("${mContext.filesDir}/c_${it.getFileName()}").exists()
                        val state = when {
                            cExists -> DownloadableFile.State.FINISHED
                            else -> DownloadableFile.State.NONE
                        }

                        it.downloadableFile = DownloadableFile.fromMediaObject(it, state)
                    }
                    it
                }
    }

    fun getHelp(): Observable<Help> =
            Observable.just(mSpRepository.getHelp())
                    .flatMap { help ->
                        if (help.content == null) {
                            return@flatMap mNetworkRepository.getHelp()
                                    .doOnNext {
                                        mSpRepository.setHelp(it)
                                    }
                        }
                        Observable.just(mSpRepository.getHelp())
                    }

    fun getMediaCacheSize(): Single<Long> {
        return Single.fromCallable {
            mContext.filesDir.listFiles().fold(0L, { acc, file -> acc + file.length() })
        }
    }

    fun clearMediaCache(): Single<Boolean> {
        return Single.fromCallable {
            mContext.filesDir.listFiles().map { it.delete() }.all { it }
        }
    }

    fun loadNotifications(): Observable<List<NotificationMessagePreview>> {
        return credentials()
                .flatMap { mNetworkRepository.getNotifications(it.first, it.second) }
    }

    fun loadNotification(id: Long): Observable<NotificationMessage> {
        return credentials()
                .flatMap { mNetworkRepository.getNotification(it.first, it.second, id) }
    }

    fun getNotificationUrl( id: Long): Observable<NotificationURL> {
        return mNetworkRepository.getNotificationUrl(id)
    }

    fun getFavorites(): Observable<List<Favorite>> {
        return credentials()
                .flatMap {mNetworkRepository.getFavorites(it.first, it.second)}
                .doOnNext {
                    mSpRepository.userFavorites(it)
                }
    }

    fun getFavoritesCash(): Single<List<Favorite>> {
        return  Single.fromCallable { mSpRepository.getFavorite()}
    }

    fun changeFavorite(action: String, id: String?, id_bookmark:String?): Observable<ResponseFavorite> {
        return credentials()
                .flatMap {mNetworkRepository.changeFavorite(it.first, it.second, action, id, id_bookmark)}

    }

    fun createLessonMessage(lessonId: Long, message: String): Single<Boolean> {
        return Observable
                .zip(
                        credentials(),
                        userData(),
                        BiFunction<Pair<String, String>, Pair<String?, UserData?>, Pair<Pair<String, String>, Long>> { credentials, userData ->
                            Pair(credentials, userData.second!!.id)
                        }
                )
                .singleOrError()
                .flatMap {
                    mNetworkRepository.createLessonMessage(it.first.first, it.first.second, it.second, lessonId, message)
                            .map { it.isSuccess() }
                }
    }

    fun getUserAgent(): Single<String> {
        return userData()
                .firstOrError()
                .map {
                    if (it.first == null || it.second == null) {
                        ExoUtils.DEFAULT_USER_AGENT
                    } else {
                        "{\"${it.second!!.id}\":\"${Hash.md5(it.first!!)}\"}"
                    }
                }
    }

    fun createReminder(contactId: Long, goodsId: Long?, lessonId: Long?, seconds: Long, mediaObjectId: Long): Observable<ReminderResponse> {
        return Observable
                .zip(
                        credentials(),
                        userData(),
                        BiFunction<Pair<String, String>, Pair<String?, UserData?>, Pair<Pair<String, String>, Long>> { credentials, userData ->
                            Pair(credentials, userData.second!!.id)
                        }
                )
                .flatMap {
                    mNetworkRepository.createReminder(it.first.first, it.first.second, contactId, goodsId, lessonId, seconds, mediaObjectId)
                }
    }

    private fun subsFcm(idContact: Long): Single<Unit> {
        val token = FirebaseInstanceId.getInstance().token
        return if (token == null) Single.just(Unit) else mNetworkRepository.subsFcm(getDeviceId(), idContact, token)
    }

    private fun unSubsFcm(): Single<Unit> {
        val token = FirebaseInstanceId.getInstance().token
        return if (token == null) Single.just(Unit) else mNetworkRepository.unSubsFcm(getDeviceId())
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceId(): String {
        return Settings.Secure.getString(mContext.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun unreadNotifications(): Single<Int> {
        return mNetworkRepository.unreadNotifications(getDeviceId())
    }

    fun setDraft(lessonId: Long, message: String?): Single<Boolean> {
        return mSpRepository.setDraft(lessonId, message)
    }

    fun getDraft(lessonId: Long): Single<String> {
        return mSpRepository.getDraft(lessonId)
    }

    fun deleteReminder(contactId: Long, mediaObjectId: Long, goodsId: Long?, lessonId: Long?): Observable<Unit> {
        return Observable.zip(
                credentials(),
                userData(),
                BiFunction<Pair<String, String>, Pair<String?, UserData?>, Pair<Pair<String, String>, Long>> { credentials, userData ->
                    Pair(credentials, userData.second!!.id)
                }
        ).flatMap {
            mNetworkRepository.deleteReminder(it.first.first, it.first.second, contactId, goodsId, lessonId, mediaObjectId)
        }
    }

    fun blockRatingRequest(temporary: Boolean): Single<Long> {
        val timestamp = if (temporary) Date().time else Long.MAX_VALUE
        return mSpRepository.markRatingRequest(timestamp)
    }

    fun needRatingRequest(): Single<Boolean> {
        return mSpRepository.getRatingRequestTimestamp()
                .flatMap {
                    if (it == 0L) blockRatingRequest(true)
                    else Single.just(it)
                }
                .map {
                    val now = Date().time
                    it != Long.MAX_VALUE && it + RATING_REQUEST_PERIOD < now
                }
    }

    fun getCards(): Single<List<Card>> {
        return credentials()
                .singleOrError()
                .flatMap { mNetworkRepository.getCards(it.first, it.second) }
                .map { it.filter { it.isUsed() } }
    }

    fun removeCard(card: Card): Single<Unit> {
        return credentials()
                .singleOrError()
                .flatMap { mNetworkRepository.removeCard(it.first, it.second, card.id) }
    }

    fun callPayment(offerId: Long): Single<CallPaymentResponse> {
        return getCards().map { if (it.isEmpty()) 2 else 1 }
                .flatMap { action -> credentials().singleOrError().map { Pair(it, action) } }
                .flatMap { mNetworkRepository.callPayment(it.first.first, it.first.second, offerId, it.second) }
    }

    companion object {
        val RATING_REQUEST_PERIOD = TimeUnit.DAYS.toMillis(7)
    }

}