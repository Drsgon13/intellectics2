package proglife.com.ua.intellektiks.business

import android.accounts.AuthenticatorException
import android.content.Context
import io.reactivex.Observable
import io.reactivex.Single
import proglife.com.ua.intellektiks.data.models.*
import proglife.com.ua.intellektiks.data.repositories.NetworkRepository
import proglife.com.ua.intellektiks.data.repositories.SPRepository
import proglife.com.ua.intellektiks.extensions.DownloadableFile
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
                    Observable.mergeDelayError (
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
    fun userData(): Single<Pair<String?, UserData?>> {
        return Single.fromCallable {
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
                            Observable.fromCallable { mSpRepository.getLessonPreviews(id) ?: emptyList() },
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
                .filter { it is Lesson}
                .cast(Lesson::class.java)
    }

    fun existsFiles(goods: Goods): Observable<Goods> {
        return Observable.just(goods)
                .map {
                    it.playerElements.forEach {
                        val cExists = File("${mContext.filesDir}/c_${it.getFileName()}").exists()
                        val state = when {
                            cExists -> DownloadableFile.State.FINISHED
                            else -> DownloadableFile.State.AWAIT
                        }
                        it.downloadableFile = DownloadableFile.fromMediaObject(it, state)
                    }
                    it
                }
    }
    fun getHelp() : Observable<Help> = mNetworkRepository.getHelp().toObservable()
}