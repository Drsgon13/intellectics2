package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName

/*

{
    "notification": {
        "url": "https://google.ru",
        "title": "Message",
        "body": "Test",
        "icon": "https://sheremetev.aoserver.ru/images/push/sheremetev1456842567.jpg",
        "tag": "sheremetev.aoserver.ru",
        "image": "https://sheremetev.aoserver.ru/images/push/sheremetev1456842567.jpg",
        "interaction": "1"
    },
    "data": {
        "type": "2",
        "id_message": "24118120"
    }
}
 */

data class NotificationURL(
        @SerializedName("notification") val notification: Notification
){

    data class Notification(
            @SerializedName("url")val url: String,
            @SerializedName("title")val title: String,
            @SerializedName("body")val body: String,
            @SerializedName("icon")val icon: String,
            @SerializedName("tag")val tag: String,
            @SerializedName("image")val image: String,
            @SerializedName("interaction")val interaction: String
    )
}