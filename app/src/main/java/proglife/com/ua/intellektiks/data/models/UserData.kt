package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName
import proglife.com.ua.intellektiks.BuildConfig

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class UserData(
        @SerializedName("id_contact") val id: Long,
        @SerializedName("last_name") val lastName: String,
        @SerializedName("name") val firstName: String,
        @SerializedName("middle_name") val middleName: String,
        @SerializedName("image") private val image: String,
        @SerializedName("phone_number") val phoneNumber: String
) {
    fun image(): String {
        return BuildConfig.SERVER + "images/contacts/pics/" + image
    }

    fun fullName(): String {
        return "$lastName $firstName"
    }
}