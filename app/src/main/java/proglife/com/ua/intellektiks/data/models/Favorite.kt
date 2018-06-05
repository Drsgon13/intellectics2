package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName

data class Favorite(
        @SerializedName("name") val name: String,
        @SerializedName("id_goods") val idGoods: String,
        @SerializedName("id") val id: String
)