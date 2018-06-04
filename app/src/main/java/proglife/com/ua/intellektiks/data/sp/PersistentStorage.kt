package proglife.com.ua.intellektiks.data.sp

import android.content.Context
import android.content.SharedPreferences
import proglife.com.ua.intellektiks.utils.Generator
import java.util.*

/**
 * Created by Evhenyi Shcherbyna on 31.10.2017.
 * Copyright (c) 2017 ProgLife. All rights reserved.
 */
class PersistentStorage(context: Context) {

    private val sp: SharedPreferences

    var mLogin: String? = null
    var mPassword: String? = null
    var mKey: String? = null
    var mUserData: String? = null
    var mCache: String? = null
    var mDrafts: String? = null
    var mRatingRequestTimestamp: Long = 0

    init {
        this.sp = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
        load()
        if (mKey == null) {
            mKey = Generator.randomString(32)
            save()
        }
    }

    private fun load() {
        mLogin = sp.getString(LOGIN, null)
        mPassword = sp.getString(PASSWORD, null)
        mKey = sp.getString(KEY, null)
        mUserData = sp.getString(USER_DATA, null)
        mCache = sp.getString(CACHE, null)
        mDrafts = sp.getString(DRAFTS, null)
        mRatingRequestTimestamp = sp.getLong(RATING_REQUEST, 0)
    }

    fun save() {
        val edit = sp.edit()
        edit.putString(LOGIN, mLogin)
        edit.putString(PASSWORD, mPassword)
        edit.putString(KEY, mKey)
        edit.putString(USER_DATA, mUserData)
        edit.putString(CACHE, mCache)
        edit.putString(DRAFTS, mDrafts)
        edit.putLong(RATING_REQUEST, mRatingRequestTimestamp)
        edit.apply()
    }

    companion object {
        const val FILENAME = "storage"

        private const val LOGIN = "001"
        private const val PASSWORD = "002"
        private const val KEY = "003"
        private const val USER_DATA = "004"
        private const val CACHE = "005"
        private const val DRAFTS = "006"
        private const val RATING_REQUEST = "007"
    }

}
