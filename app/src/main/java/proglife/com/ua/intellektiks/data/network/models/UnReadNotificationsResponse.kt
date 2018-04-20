package proglife.com.ua.intellektiks.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 20.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class UnReadNotificationsResponse(
        @SerializedName("error") val error: Int
)