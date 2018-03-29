package proglife.com.ua.intellektiks.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class LessonPreview(
        @SerializedName("id_training_lessons") val id: Long,
        @SerializedName("training_lessons") val name: String,
        @SerializedName("description") val description: String,
        @SerializedName("day") val day: Int,
        @SerializedName("number") val number: Int,
        @SerializedName("request_report") val requestReport: String,
        @SerializedName("check_report") val checkReport: String,
        @SerializedName("access") val access: Boolean
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(day)
        parcel.writeInt(number)
        parcel.writeString(requestReport)
        parcel.writeString(checkReport)
        parcel.writeByte(if (access) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LessonPreview> {
        override fun createFromParcel(parcel: Parcel): LessonPreview {
            return LessonPreview(parcel)
        }

        override fun newArray(size: Int): Array<LessonPreview?> {
            return arrayOfNulls(size)
        }
    }
}