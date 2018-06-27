package proglife.com.ua.intellektiks.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 20.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class UnReadNotificationsRequest(
        @SerializedName("id_device") val deviceId: String,
        @SerializedName("login") val login: String,
        @SerializedName("password") val password: String
) : NetworkRequest(ServerMethod.COUNT_NOTIFICATION)