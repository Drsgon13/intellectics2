package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName
import proglife.com.ua.intellektiks.data.models.MediaObject.CREATOR.DIVIDER_BOOT_TYPE
import java.util.*

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class Goods(
        @SerializedName("id_contact") val contactId: Long,
        @SerializedName("id_goods") val id: Long,
        @SerializedName("creation_date") val creationDate: Date,
        @SerializedName("goods") val name: String,
        @SerializedName("price") val price: Double,
        @SerializedName("information_for_personal") val informationForPersonal: String,
//        @SerializedName("common_elements") val commonElements: String,
        @SerializedName("data_common_elements") val commonElements: List<MediaObject>,
//        @SerializedName("massive_elements_html") val massiveElementsHtml: String,
        @SerializedName("player_elements") val playerElements: List<MediaObject>,
        @SerializedName("toggles_html") val togglesHtml: String,
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