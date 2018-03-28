package proglife.com.ua.intellektiks.data.repositories

import android.content.Context
import com.google.gson.Gson
import proglife.com.ua.intellektiks.data.models.UserData
import proglife.com.ua.intellektiks.data.sp.LocalStorage
import proglife.com.ua.intellektiks.utils.AESCrypt

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class SPRepository(context: Context,
                   val mGson: Gson,
                   val mAesCrypt: AESCrypt) {

    private val mLocalStorage = LocalStorage(context)
    private val mKey = "b36dB${mLocalStorage.mKey}ls9Nd0"

    fun credentials(pair: Pair<String?, String?>, save: Boolean) {
        mLocalStorage.mLogin = pair.first?.let { mAesCrypt.encrypt(mKey, it) }
        mLocalStorage.mPassword = pair.second?.let { mAesCrypt.encrypt(mKey, it) }
        if (save) mLocalStorage.save()
    }

    fun credentials(): Pair<String?, String?> {
        val login = mLocalStorage.mLogin?.let { mAesCrypt.decrypt(mKey, it) }
        val password = mLocalStorage.mPassword?.let { mAesCrypt.decrypt(mKey, it) }
        return Pair(login, password)
    }

    fun userData(data: UserData?, save: Boolean) {
        mLocalStorage.mUserData = data?.let { mAesCrypt.encrypt(mKey, mGson.toJson(it)) }
        if (save) mLocalStorage.save()
    }

    fun userData(): UserData? {
        val userData = mLocalStorage.mUserData
        return userData?.let { mGson.fromJson(mAesCrypt.decrypt(mKey, it), UserData::class.java) }
    }

}