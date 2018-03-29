package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class MediaObject(
    @SerializedName("boottype") val bootType: Long,
    @SerializedName("filetype") val fileType: FileType = FileType.UNKNOWN,
    @SerializedName("url") val url: String,
    @SerializedName("title") val title: String,
    @SerializedName("size") val size: String,
    @SerializedName("lenght") val length: String,
    @SerializedName("mediaobjectid") val id: Long
)