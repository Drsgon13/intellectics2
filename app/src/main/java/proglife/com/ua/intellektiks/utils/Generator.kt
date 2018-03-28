package proglife.com.ua.intellektiks.utils

import java.util.*

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
object Generator {

    const val DATA = "0123456789aAbBcCdDeEfFgGhHiIjJkKLMNOPQRSTUVWXYZ"
    val RANDOM = Random()

    fun randomString(len: Int): String {
        val sb = StringBuilder(len)
        for (i in 0 until len) {
            sb.append(DATA[RANDOM.nextInt(DATA.length)])
        }
        return sb.toString()
    }

}
