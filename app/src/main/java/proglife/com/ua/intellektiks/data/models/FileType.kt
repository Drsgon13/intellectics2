package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
enum class FileType {
    @SerializedName("UNKNOWN") UNKNOWN,
    @SerializedName("HLS") HLS,
    @SerializedName("MP4") MP4,
    @SerializedName("MP3") MP3
}