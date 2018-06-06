package proglife.com.ua.intellektiks.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 05.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class GetCardsRequest(
        @SerializedName("login") val login: String,
        @SerializedName("password") val password: String
): NetworkRequest(ServerMethod.GET_CARDS)