package proglife.com.ua.intellektiks.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
enum class ServerMethod {
    @SerializedName("getUserData") GET_USER_DATA,
    @SerializedName("getUserGoods") GET_USER_GOODS,
    @SerializedName("getGood") GET_GOODS;
}