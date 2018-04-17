package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class Lesson(
        @SerializedName("id_training_lessons") val id: Long,
        @SerializedName("id_contact") val idContact: Long,
        @SerializedName("description") val description: String?,
//        @SerializedName("common_elements") val commonElements: String,
        @SerializedName("player_elements") val playerElements: List<MediaObject>,
        @SerializedName("data_common_elements") val commonElements: List<MediaObject>,
        @SerializedName("messages") val messages: List<LessonMessage>,
        @SerializedName("toggles_massive") val togglesMassive: List<Marker>?
) {
    fun getMediaObjects(): List<MediaObject> {
        val playerList = playerElements
                .filter { it.fileType != FileType.JPG }
                .map {
                    it.type = if (it.bootType == MediaObject.DIVIDER_BOOT_TYPE) MediaObject.Type.DIVIDER else MediaObject.Type.PLAYER
                    it.downloadable = it.bootType != MediaObject.DIVIDER_BOOT_TYPE
                    it
                }
        val commonList = commonElements
                .map {
                    it.type = if (it.bootType == MediaObject.DIVIDER_BOOT_TYPE) MediaObject.Type.DIVIDER else MediaObject.Type.COMMON
                    it.downloadable = MediaObject.DOWNLOADABLE_BOOT_TYPE.contains(it.bootType)
                    it
                }
        return if (playerList.isNotEmpty() && commonList.isNotEmpty()) {
            playerList.plus(MediaObject.getDividerInstance()).plus(commonList)
        } else playerList.plus(commonList)
    }
}