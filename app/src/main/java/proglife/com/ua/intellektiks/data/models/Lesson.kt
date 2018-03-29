package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class Lesson(
        @SerializedName("id_training_lessons") val id: Long,
        @SerializedName("description") val description: String,
        @SerializedName("common_elements") val commonElements: String,
        @SerializedName("player_elements") val mediaObjects: List<MediaObject>
) {
    fun getMediaObjects(vararg fileType: FileType): List<MediaObject> {
        return mediaObjects.filter { fileType.contains(it.fileType) }
    }
}