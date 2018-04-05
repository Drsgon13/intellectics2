package proglife.com.ua.intellektiks.extensions

import android.os.Parcel
import android.os.Parcelable
import proglife.com.ua.intellektiks.data.models.MediaObject

/**
 * Created by Evhenyi Shcherbyna on 30.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class DownloadableFile(
        val id: Long,
        val url: String,
        val name: String,
        var state: State = State.NONE,
        var progress: Int = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readSerializable() as State,
            parcel.readInt())

    enum class State {
        NONE,
        AWAIT,
        PROCESSING,
        FINISHED,
        FAILED
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(url)
        parcel.writeString(name)
        parcel.writeSerializable(state)
        parcel.writeInt(progress)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DownloadableFile> {

        fun fromMediaObject(mediaObject: MediaObject, state: State = State.AWAIT): DownloadableFile {
            return DownloadableFile(mediaObject.id, mediaObject.url, mediaObject.getFileName(), state)
        }

        override fun createFromParcel(parcel: Parcel): DownloadableFile {
            return DownloadableFile(parcel)
        }

        override fun newArray(size: Int): Array<DownloadableFile?> {
            return arrayOfNulls(size)
        }
    }
}