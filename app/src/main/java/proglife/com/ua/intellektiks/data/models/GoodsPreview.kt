package proglife.com.ua.intellektiks.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class GoodsPreview(
        @SerializedName("id_goods") val id: Long,
        @SerializedName("goods") val name: String,
        @SerializedName("creation_date") val creationDate: Date,
        @SerializedName("image") val image: String,
        @SerializedName("id_training") val trainingId: Long?
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            Date(parcel.readLong()),
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeLong(creationDate.time)
        parcel.writeString(image)
        parcel.writeValue(trainingId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GoodsPreview> {
        override fun createFromParcel(parcel: Parcel): GoodsPreview {
            return GoodsPreview(parcel)
        }

        override fun newArray(size: Int): Array<GoodsPreview?> {
            return arrayOfNulls(size)
        }
    }

}