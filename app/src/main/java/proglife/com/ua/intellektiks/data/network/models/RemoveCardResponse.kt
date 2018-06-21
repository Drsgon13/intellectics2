package proglife.com.ua.intellektiks.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 21.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class RemoveCardResponse(
        @SerializedName("success") val message: String
)