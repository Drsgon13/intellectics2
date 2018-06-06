package proglife.com.ua.intellektiks.data.network.models

import com.google.gson.annotations.SerializedName

class SetFavoritesRequest(
        @SerializedName("login") val login: String,
        @SerializedName("password") val password: String,
        @SerializedName("action") val action: String,
        @SerializedName("id_goods") val id: String?,
        @SerializedName("id_bookmark") val id_bookmark: String?
) : NetworkRequest(ServerMethod.SET_FAVORITES) {

    companion object {
        const val ADD = "add"
        const val DELETE = "deleted"
    }
}