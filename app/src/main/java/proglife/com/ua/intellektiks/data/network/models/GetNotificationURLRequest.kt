package proglife.com.ua.intellektiks.data.network.models

import com.google.gson.annotations.SerializedName

class GetNotificationURLRequest(
        @SerializedName("id_message") val id: Long
): NetworkRequest(ServerMethod.GET_NOTIFICATION)