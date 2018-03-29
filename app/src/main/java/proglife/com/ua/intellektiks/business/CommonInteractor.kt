package proglife.com.ua.intellektiks.business

import android.accounts.AuthenticatorException
import io.reactivex.Single
import proglife.com.ua.intellektiks.data.models.*
import proglife.com.ua.intellektiks.data.repositories.NetworkRepository
import proglife.com.ua.intellektiks.data.repositories.SPRepository

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class CommonInteractor(
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

    fun loadData(): Single<List<GoodsPreview>> {
        return Single.just(mSpRepository.credentials())
                .flatMap {
                    if (it.first == null || it.second == null) {
                        throw AuthenticatorException()
                    }
                    mNetworkRepository.getUserGoods(it.first!!, it.second!!)
                }
    }

    fun userData(): Single<Pair<String?, UserData?>> {
        return Single.fromCallable {
            Pair(mSpRepository.credentials().first, mSpRepository.userData())
        }
    }

    fun getGoods(id: Long): Single<Goods> {
        return Single.fromCallable { mSpRepository.credentials() }
                .flatMap { mNetworkRepository.getGoods(it.first!!, it.second!!, id) }
    }

    fun getLessons(id: Long): Single<List<LessonPreview>> {
        return Single.fromCallable { mSpRepository.credentials() }
                .flatMap { mNetworkRepository.getLessons(it.first!!, it.second!!, id) }
    }

    fun getLesson(id: Long): Single<Lesson> {
        return Single.fromCallable { mSpRepository.credentials() }
                .flatMap { mNetworkRepository.getLesson(it.first!!, it.second!!, id) }
    }

}