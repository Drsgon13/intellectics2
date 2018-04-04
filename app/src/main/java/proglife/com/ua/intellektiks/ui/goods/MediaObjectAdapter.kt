package proglife.com.ua.intellektiks.ui.goods

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import kotlinx.android.synthetic.main.li_media_object.view.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.utils.PositionListener

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class MediaObjectAdapter(private val mOnSelectMediaObjectListener: OnSelectMediaObjectListener, private val positionListener: PositionListener):
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mList: List<MediaObject> = emptyList()
    private lateinit var mContext: Context

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mContext = recyclerView.context
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MediaObjectViewHolder(LayoutInflater.from(mContext).inflate(R.layout.li_media_object, parent, false))
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MediaObjectViewHolder) {
            val mediaObject: MediaObject = mList[position]
            holder.tvName.text = mediaObject.title
            holder.tvName.setOnClickListener { positionListener.onClickPosition(position)}
            holder.tvInfo.text = mContext.getString(R.string.file_info, mediaObject.size)
            holder.btnDownload.setOnClickListener { mOnSelectMediaObjectListener.onDownload(mediaObject) }
        }
    }

    class MediaObjectViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.tvName
        val btnDownload: ImageButton = itemView.btnDownload
        val tvInfo: TextView = itemView.tvInfo
    }

    fun show(mediaObjects: List<MediaObject>) {
        mList = mediaObjects
        notifyDataSetChanged()
    }

    interface OnSelectMediaObjectListener {
        fun onDownload(mediaObject: MediaObject)
    }

}