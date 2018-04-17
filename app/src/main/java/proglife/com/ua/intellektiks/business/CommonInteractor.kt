package proglife.com.ua.intellektiks.business

import android.accounts.AuthenticatorException
import android.content.Context
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import proglife.com.ua.intellektiks.data.models.*
import proglife.com.ua.intellektiks.data.network.models.CreateReminderRequest
import proglife.com.ua.intellektiks.data.network.models.ReminderResponse
import proglife.com.ua.intellektiks.data.repositories.NetworkRepository
import proglife.com.ua.intellektiks.data.repositories.SPRepository
import proglife.com.ua.intellektiks.extensions.DownloadableFile
import proglife.com.ua.intellektiks.utils.ExoUtils
import proglife.com.ua.intellektiks.utils.Hash
import java.io.File

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
                .doOnSuccess {
                    mSpRepository.credentials(Pair(login, password), remember)
                    mSpRepository.userData(it, remember)
                }
    }

    fun logout(): Single<Unit> {
        return Single.fromCallable {
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

    fun getLesson(id: Long): Observable<Lesson> {
        return credentials()
                .flatMap {
                    Observable.mergeDelayError(
                            Observable.fromCallable { mSpRepository.getLesson(id) ?: Unit },
                            mNetworkRepository.getLesson(it.first, it.second, id)
                                    .doOnNext {
                                        mSpRepository.setLesson(id, it)
                                    }
                    )
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

    fun createLessonMessage(lessonId: Long, message: String): Observable<Unit> {
        return Observable.zip(
                credentials(),
                userData(),
                BiFunction<Pair<String, String>, Pair<String?, UserData?>, Pair<Pair<String, String>, Long>> {
                    credentials, userData -> Pair(credentials, userData.second!!.id)
                }
        ).flatMap {
            mNetworkRepository.createLessonMessage(it.first.first, it.first.second, it.second, lessonId, message)
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

    fun createReminder( contactId: Long, goodsId: Long?, lessonId: Long?, seconds: Long, mediaObjectId: Long): Observable<ReminderResponse> {
        return Observable.zip(
                credentials(),
                userData(),
                BiFunction<Pair<String, String>, Pair<String?, UserData?>, Pair<Pair<String, String>, Long>> {
                    credentials, userData -> Pair(credentials, userData.second!!.id)
                }
        ).flatMap {
            mNetworkRepository.createReminder(it.first.first, it.first.second, contactId, goodsId, lessonId, seconds, mediaObjectId)
        }
    }

    fun deleteReminder(contactId: Long, mediaObjectId: Long, goodsId: Long?, lessonId: Long?): Observable<Unit>{
        return Observable.zip(
                credentials(),
                userData(),
                BiFunction<Pair<String, String>, Pair<String?, UserData?>, Pair<Pair<String, String>, Long>> {
                    credentials, userData -> Pair(credentials, userData.second!!.id)
                }
        ).flatMap {
            mNetworkRepository.deleteReminder(it.first.first, it.first.second, contactId, goodsId, lessonId, mediaObjectId)
        }
    }

}