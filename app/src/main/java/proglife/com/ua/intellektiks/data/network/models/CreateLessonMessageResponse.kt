package proglife.com.ua.intellektiks.data.network.models

/**
 * Created by Evhenyi Shcherbyna on 17.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class CreateLessonMessageResponse(
        private val status: String
) {
    fun isSuccess() = status == "success"
}