package proglife.com.ua.intellektiks.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 17.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class UnSubsFcmRequest(
        @SerializedName("id_device") val deviceId: String
)