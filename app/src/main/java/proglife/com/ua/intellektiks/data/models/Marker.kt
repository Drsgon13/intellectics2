package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName

data class Marker(@SerializedName("title") val title: String,
                  @SerializedName("position") val position: Long,
                  @SerializedName("mediaobjectid") val mediaobjectid: Long,
                  @SerializedName("url_file") val urlFile: String,
                  @SerializedName("filetypeid") val filetypeid: String)