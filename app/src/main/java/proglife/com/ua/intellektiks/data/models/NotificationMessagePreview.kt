package proglife.com.ua.intellektiks.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Evhenyi Shcherbyna on 07.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class NotificationMessagePreview(
        @SerializedName("id_message") val id: Long,
        @SerializedName("message_subject") val subject: String,
        @SerializedName("message_text") val text: String,
        @SerializedName("creation_date") val creationDate: Date,
        @SerializedName("sent_date") val sentDate: Date,
        @SerializedName("productive") var productive: String,
        @SerializedName("id_offer") val offerId : Long?
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            Date(parcel.readLong()),
            Date(parcel.readLong()),
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as Long?)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(subject)
        parcel.writeString(text)
        parcel.writeLong(creationDate.time)
        parcel.writeLong(sentDate.time)
        parcel.writeString(productive)
        parcel.writeValue(offerId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationMessagePreview> {
        override fun createFromParcel(parcel: Parcel): NotificationMessagePreview {
            return NotificationMessagePreview(parcel)
        }

        override fun newArray(size: Int): Array<NotificationMessagePreview?> {
            return arrayOfNulls(size)
        }
    }
}