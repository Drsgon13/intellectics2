package proglife.com.ua.intellektiks.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 09.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class CreateLessonMessageRequest(
        @SerializedName("login") val login: String,
        @SerializedName("password") val password: String,
        @SerializedName("id_contact") val contactId: Long,
        @SerializedName("id_training_lessons") val lessonId: Long,
        @SerializedName("training_lessons_message") val message: String
): NetworkRequest(ServerMethod.CREATE_LESSON_MESSAGE)