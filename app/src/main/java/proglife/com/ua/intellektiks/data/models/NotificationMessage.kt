package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 07.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class NotificationMessage(
        @SerializedName("message_text") val text: String,
        @SerializedName("id_offer") val offerId: Long?
)