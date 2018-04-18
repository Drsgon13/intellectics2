package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 18.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
abstract class Content {

    @SerializedName("id_training_lessons", alternate = ["id_goods"]) val id: Long = -1
    @SerializedName("id_contact") val contactId: Long = -1
    @SerializedName("description", alternate = ["information_for_personal"]) val description: String? = null
    @SerializedName("player_elements") val playerElements: List<MediaObject> = emptyList()
    @SerializedName("data_common_elements") val commonElements: List<MediaObject> = emptyList()
    @SerializedName("toggles_massive") val togglesMassive: List<Marker>? = emptyList()

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