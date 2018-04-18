package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName
import proglife.com.ua.intellektiks.data.models.MediaObject.CREATOR.DIVIDER_BOOT_TYPE
import java.util.*

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class Goods(
        @SerializedName("creation_date") val creationDate: Date,
        @SerializedName("goods") val name: String
): Content()