package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 05.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class Card(
        @SerializedName("id") val id: Long,
        @SerializedName("type") val type: String,
        @SerializedName("mask") val mask: String,
        @SerializedName("used") val used: Int
) {
    fun isUsed() = used == 1
}