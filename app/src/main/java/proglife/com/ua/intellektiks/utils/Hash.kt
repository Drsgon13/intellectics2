package proglife.com.ua.intellektiks.utils

import java.security.MessageDigest

/**
 * Created by Evhenyi Shcherbyna on 16.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
object Hash {

    fun md5(s: String): String {
        val bytes = MessageDigest.getInstance("MD5").digest(s.toByteArray())
        return bytes.fold("", { str, it -> str + "%02x".format(it)})
    }

}