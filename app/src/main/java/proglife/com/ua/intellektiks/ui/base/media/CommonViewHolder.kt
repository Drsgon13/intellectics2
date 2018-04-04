package proglife.com.ua.intellektiks.ui.base.media

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.li_media_object_common.view.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.MediaObject

class CommonViewHolder(
        itemView: View,
        private val onSelectMediaObjectListener: MediaObjectAdapter.OnSelectMediaObjectListener
) : RecyclerView.ViewHolder(itemView) {
    private val mContext = itemView.context
    private val ivFormat: ImageView = itemView.ivFormat
    private val tvName: TextView = itemView.tvName
    private val tvInfo: TextView = itemView.tvInfo

    fun bind(mediaObject: MediaObject) {
        val formatRes = when {
            mediaObject.fileType == null && mediaObject.downloadable -> R.mipmap.ic_file_dl
            mediaObject.fileType == null && !mediaObject.downloadable -> R.mipmap.ic_file
            mediaObject.fileType != null && mediaObject.downloadable -> mediaObject.fileType.dlIcon
            mediaObject.fileType != null && !mediaObject.downloadable -> mediaObject.fileType.icon
            else -> R.mipmap.ic_file
        }
        ivFormat.setImageResource(formatRes)
        tvName.text = mContext.getString(if (mediaObject.downloadable) R.string.file_download else R.string.file_open, mediaObject.title)
        tvInfo.text = if (mediaObject.size.isNotBlank()) mContext.getString(R.string.file_info, mediaObject.size) else ""
        tvName.setOnClickListener {
            if (mediaObject.downloadable) {
                onSelectMediaObjectListener.onDownload(mediaObject)
            } else {
                onSelectMediaObjectListener.onSelect(mediaObject)
            }
        }
    }
}