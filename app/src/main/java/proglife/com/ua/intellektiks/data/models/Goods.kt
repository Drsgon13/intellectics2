package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName
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
        @SerializedName("common_elements") val commonElements: String,
        @SerializedName("massive_elements_html") val massiveElementsHtml: String,
        @SerializedName("player_elements") val mediaObjects: List<MediaObject>,
        @SerializedName("toggles_html") val togglesHtml: String
) {
    fun getMediaObjects(vararg fileType: FileType): List<MediaObject> {
        return mediaObjects.filter { fileType.contains(it.fileType) }
    }
}