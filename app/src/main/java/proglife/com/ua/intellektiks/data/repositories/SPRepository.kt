package proglife.com.ua.intellektiks.data.repositories

import android.content.Context
import com.google.gson.Gson
import io.reactivex.Single
import proglife.com.ua.intellektiks.data.models.*
import proglife.com.ua.intellektiks.data.sp.Cache
import proglife.com.ua.intellektiks.data.sp.MemoryStorage
import proglife.com.ua.intellektiks.data.sp.PersistentStorage
import proglife.com.ua.intellektiks.utils.AESCrypt


/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class SPRepository(context: Context,
                   private val mGson: Gson,
                   private val mAesCrypt: AESCrypt) {

    private val mPersistentStorage = PersistentStorage(context)
    private val mMemoryStorage = MemoryStorage()
    private var mCache = Cache()
    private val mKey = "b36dB${mPersistentStorage.mKey}ls9Nd0"

    init {
        mCache = mPersistentStorage.mCache?.let { mGson.fromJson(mAesCrypt.decrypt(mKey, it), Cache::class.java) } ?: Cache()
    }

    fun credentials(pair: Pair<String?, String?>, save: Boolean) {
        if (save) {
            mPersistentStorage.mLogin = pair.first?.let { mAesCrypt.encrypt(mKey, it) }
            mPersistentStorage.mPassword = pair.second?.let { mAesCrypt.encrypt(mKey, it) }
            mPersistentStorage.save()
        }
        mMemoryStorage.credentials = pair
    }

    fun credentials(): Pair<String?, String?> {
        return if (mMemoryStorage.credentials.first != null && mMemoryStorage.credentials.second != null) {
            mMemoryStorage.credentials
        } else {
            val login = mPersistentStorage.mLogin?.let { mAesCrypt.decrypt(mKey, it) }
            val password = mPersistentStorage.mPassword?.let { mAesCrypt.decrypt(mKey, it) }
            val credentials = Pair(login, password)
            mMemoryStorage.credentials = credentials
            credentials
        }
    }

    fun userData(data: UserData?, save: Boolean) {
        if (save) {
            mPersistentStorage.mUserData = data?.let { mAesCrypt.encrypt(mKey, mGson.toJson(it)) }
            mPersistentStorage.save()
        }
        mMemoryStorage.userData = data
    }

    fun userData(): UserData? {
        return if (mMemoryStorage.userData != null) {
            mMemoryStorage.userData
        } else {
            val userData = mPersistentStorage.mUserData?.let { mGson.fromJson(mAesCrypt.decrypt(mKey, it), UserData::class.java) }
            mMemoryStorage.userData = userData
            userData
        }
    }

    fun userGoods(): List<GoodsPreview>? {
        return mCache.userGoodsPreview
    }

    fun userGoods(data: List<GoodsPreview>?) {
        mCache.userGoodsPreview = data
        saveCache()
    }

    private fun saveCache() {
        mPersistentStorage.mCache = mAesCrypt.encrypt(mKey, mGson.toJson(mCache))
        mPersistentStorage.save()
    }

    fun getGoods(id: Long): Goods? {
        val goods = mCache.goodsList[id]
        goods?.let {
            it.togglesMassive = null
        }
        return goods
    }

    fun setGoods(id: Long, goods: Goods?) {
        mCache.goodsList[id] = goods
        saveCache()
    }


    fun getLessonPreviews(id: Long): List<LessonPreview>? {
        return mCache.lessonPreviews[id]
    }

    fun setLessonPreviews(id: Long, it: List<LessonPreview>?) {
        mCache.lessonPreviews[id] = it
        saveCache()
    }

    fun getLesson(id: Long): Lesson? {
        val lesson = mCache.lessons[id]
        lesson?.let {
            it.togglesMassive = null
        }
        return lesson
    }

    fun setLesson(id: Long, lesson: Lesson?) {
        mCache.lessons[id] = lesson
        saveCache()
    }

    fun setHelp(help: Help){
        mCache.help = help
        saveCache()
    }

    fun getHelp(): Help {
        return if(mCache.help == null) Help(null) else mCache.help!!
    }

    fun getDraft(lessonId: Long): Single<String> {
        return Single.just("")
    }

    fun removeDraft(lessonId: Long): Single<Unit> {
        return Single.just(Unit)
    }

    fun setDraft(lessonId: Long, message: String): Single<Unit> {
        return Single.just(Unit)
    }
}