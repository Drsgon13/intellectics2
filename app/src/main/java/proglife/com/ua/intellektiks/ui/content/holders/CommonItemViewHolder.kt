package proglife.com.ua.intellektiks.ui.content.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.android.synthetic.main.li_media_object_common.view.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.FileType
import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.extensions.DownloadableFile
import proglife.com.ua.intellektiks.ui.content.adapters.ContentAdapter

class CommonItemViewHolder(
        itemView: View,
        private val onMediaObjectAction: ContentAdapter.OnMediaObjectAction?
) : RecyclerView.ViewHolder(itemView) {
    private val mContext = itemView.context
    private val ivFormat: ImageView = itemView.ivFormat
    private val pbDownload: ProgressBar = itemView.pbDownload
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
        tvName.text = mContext.getString(if (mediaObject.downloadable) R.string.file_download else
            if(mediaObject.fileType == FileType.PDF || mediaObject.fileType == FileType.DOC || mediaObject.fileType == FileType.TXT)
                R.string.file_read
            else R.string.file_open, mediaObject.title)
        tvInfo.text = if (mediaObject.size.isNotBlank()) mContext.getString(R.string.file_info, mediaObject.size) else ""
        val showLoading: Boolean = mediaObject.downloadableFile?.state == DownloadableFile.State.PROCESSING ||
                mediaObject.downloadableFile?.state == DownloadableFile.State.AWAIT
        ivFormat.visibility = if (showLoading) View.GONE else View.VISIBLE
        pbDownload.visibility = if (showLoading) View.VISIBLE else View.GONE
        tvName.setOnClickListener {
            if (ivFormat.visibility == View.VISIBLE) {
                if (mediaObject.downloadable) {
                    onMediaObjectAction?.onDownload(mediaObject)
                } else {
                    onMediaObjectAction?.onSelect(mediaObject)
                }
            }
        }
    }
}