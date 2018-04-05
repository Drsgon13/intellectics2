package proglife.com.ua.intellektiks.ui.base.media

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.android.synthetic.main.li_media_object_player.view.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.FileType
import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.extensions.DownloadableFile

class PlayerViewHolder(
        itemView: View,
        private val mOnSelectMediaObjectListener: MediaObjectAdapter.OnSelectMediaObjectListener
) : RecyclerView.ViewHolder(itemView) {
    private val mContext = itemView.context
    val tvName: TextView = itemView.tvName
    val btnDownload: ImageButton = itemView.btnDownload
    val downloadFrame: View = itemView.downloadFrame
    val tvInfo: TextView = itemView.tvInfo
    val pbDownload: ProgressBar = itemView.pbDownload

    fun bind(mediaObject: MediaObject) {
        val linkColor = if (mediaObject.downloadableFile?.state == DownloadableFile.State.FINISHED)
            R.color.colorTitleGreenText else R.color.colorTitleBlueText
        val buttonColor = when (mediaObject.downloadableFile?.state) {
            DownloadableFile.State.FINISHED -> R.color.colorTitleGreenText
            DownloadableFile.State.FAILED -> R.color.colorTitleRedText
            else -> R.color.colorTitleBlueText
        }
        tvName.setTextColor(ContextCompat.getColor(mContext, linkColor))
        tvName.text = mediaObject.title
        tvName.setOnClickListener { mOnSelectMediaObjectListener.onSelect(mediaObject) }
        tvInfo.text = if (mediaObject.size.isNotBlank()) mContext.getString(R.string.file_info, mediaObject.size) else ""
        if (mediaObject.fileType == FileType.HLS) {
            downloadFrame.visibility = View.GONE
            tvInfo.visibility = View.GONE
        } else {
            if (downloadFrame.visibility == View.GONE) downloadFrame.visibility = View.VISIBLE
            if (tvInfo.visibility == View.GONE) tvInfo.visibility = View.VISIBLE
            val showLoading: Boolean = mediaObject.downloadableFile?.state == DownloadableFile.State.PROCESSING ||
                    mediaObject.downloadableFile?.state == DownloadableFile.State.AWAIT
            btnDownload.visibility = if (showLoading) View.GONE else View.VISIBLE
            pbDownload.visibility = if (showLoading) View.VISIBLE else View.GONE
            btnDownload.setOnClickListener { mOnSelectMediaObjectListener.onDownload(mediaObject) }
            btnDownload.setColorFilter(ContextCompat.getColor(mContext, buttonColor))
        }
    }

}