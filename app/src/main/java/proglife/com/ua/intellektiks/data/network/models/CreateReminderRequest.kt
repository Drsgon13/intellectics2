package proglife.com.ua.intellektiks.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 12.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class CreateReminderRequest(
        @SerializedName("id_contact") val contactId: Long,
        @SerializedName("id_goods") val goodsId: Long?,
        @SerializedName("id_training_lessons") val lessonId: Long?,
        @SerializedName("position") val seconds: Long,
        @SerializedName("mediaobjectid") val mediaObjectId: Long
)