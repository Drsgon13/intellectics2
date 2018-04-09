package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Evhenyi Shcherbyna on 09.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class LessonMessage(
        @SerializedName("id_training_lessons_message") val id: Long,
        @SerializedName("training_lessons_message") val message: String,
        @SerializedName("id_training_lessons_message_type") val type: Int,
        @SerializedName("id_training_lessons_message_status") val status: Int,
        @SerializedName("creation_date") val creationDate: Date
)