package proglife.com.ua.intellektiks.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class UpdateNotificationsRequest(
        @SerializedName("login") val login: String,
        @SerializedName("password") val password: String,
        @SerializedName("id_message") val id: Long
): NetworkRequest(ServerMethod.UPDATE_NOTIFICATIONS)