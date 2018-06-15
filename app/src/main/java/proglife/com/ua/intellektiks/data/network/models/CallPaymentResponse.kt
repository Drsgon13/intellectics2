package proglife.com.ua.intellektiks.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 15.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class CallPaymentResponse(
        @SerializedName("link") val link: String?,
        @SerializedName("success") val success: String?
)