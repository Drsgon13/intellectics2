package proglife.com.ua.intellektiks.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import proglife.com.ua.intellektiks.extensions.DownloadableFile

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class MediaObject(
    @SerializedName("boottype") val bootType: Long,
    @SerializedName("filetype") val fileType: FileType?,
    @SerializedName("url") val url: String,
    @SerializedName("title") val title: String,
    @SerializedName("size") val size: String,
    @SerializedName("lenght") val length: String,
    @SerializedName("description") val description: String,
    @SerializedName("mediaobjectid") val id: Long,
    var downloadable: Boolean,
    var type: Type,
    var downloadableFile: DownloadableFile? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readSerializable() as FileType,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readInt() == 1,
            parcel.readSerializable() as Type)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(bootType)
        parcel.writeSerializable(fileType)
        parcel.writeString(url)
        parcel.writeString(title)
        parcel.writeString(size)
        parcel.writeString(length)
        parcel.writeString(description)
        parcel.writeLong(id)
        parcel.writeInt(if (downloadable) 1 else 0)
        parcel.writeSerializable(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getFileName(): String {
        return "$id.${fileType?.name}"
    }

    companion object CREATOR : Parcelable.Creator<MediaObject> {
        const val DIVIDER_BOOT_TYPE = 21L
        val DOWNLOADABLE_BOOT_TYPE: LongArray = longArrayOf(1L, 2L, 6L, 7L, 8L, 9L, 10L, 11L, 13L, 18L, 29L, 30L, 31L)

        override fun createFromParcel(parcel: Parcel): MediaObject {
            return MediaObject(parcel)
        }

        override fun newArray(size: Int): Array<MediaObject?> {
            return arrayOfNulls(size)
        }

        fun getDividerInstance(): MediaObject {
            return MediaObject(DIVIDER_BOOT_TYPE, null, "", "", "", "", "", 0, false, Type.DIVIDER)
        }
    }

    enum class Type {
        COMMON,
        PLAYER,
        DIVIDER
    }

}