package proglife.com.ua.intellektiks.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 15.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class CallPaymentRequest(
        @SerializedName("login") val login: String,
        @SerializedName("password") val password: String,
        @SerializedName("id_offer") val offerId: Long,
        @SerializedName("action") val action: Int
) : NetworkRequest(ServerMethod.CALL_PAYMENT)