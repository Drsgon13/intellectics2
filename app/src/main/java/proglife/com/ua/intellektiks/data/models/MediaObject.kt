package proglife.com.ua.intellektiks.data.models

import android.os.Parcel
import android.os.Parcelable
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
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readSerializable() as FileType,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(bootType)
        parcel.writeSerializable(fileType)
        parcel.writeString(url)
        parcel.writeString(title)
        parcel.writeString(size)
        parcel.writeString(length)
        parcel.writeLong(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getName(): String {
        return "$id.${fileType.name}"
    }

    companion object CREATOR : Parcelable.Creator<MediaObject> {
        override fun createFromParcel(parcel: Parcel): MediaObject {
            return MediaObject(parcel)
        }

        override fun newArray(size: Int): Array<MediaObject?> {
            return arrayOfNulls(size)
        }
    }
}