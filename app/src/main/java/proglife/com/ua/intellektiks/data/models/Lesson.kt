package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class Lesson(
        @SerializedName("messages") val messages: List<ReportMessage>
): Content()