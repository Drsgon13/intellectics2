package proglife.com.ua.intellektiks.data.network.models

import com.google.gson.annotations.SerializedName

data class DeleteReminderRequest(@SerializedName("login") val login: String,
                                 @SerializedName("password") val password: String,
                                 @SerializedName("id_contact") val contactId: Long,
                                 @SerializedName("id_goods") val goodsId: Long?,
                                 @SerializedName("id_training_lessons") val lessonId: Long?,
                                 @SerializedName("mediaobjectid") val mediaObjectId: Long) {
}