package proglife.com.ua.intellektiks.data.network.models

import com.google.gson.annotations.SerializedName

class GetFavoritesRequest(
        @SerializedName("login") val login: String,
        @SerializedName("password") val password: String
): NetworkRequest(ServerMethod.GET_FAVORITES)