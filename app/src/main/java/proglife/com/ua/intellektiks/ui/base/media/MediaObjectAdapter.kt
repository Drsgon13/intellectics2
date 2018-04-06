package proglife.com.ua.intellektiks.ui.base.media

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.utils.inflate

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class MediaObjectAdapter(private val mOnSelectMediaObjectListener: OnSelectMediaObjectListener):
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mList: List<MediaObject> = emptyList()
    private lateinit var mContext: Context
    private var mSelectedItem: MediaObject? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mContext = recyclerView.context
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MediaObject.Type.COMMON.ordinal -> CommonViewHolder(parent.inflate(R.layout.li_media_object_common), mOnSelectMediaObjectListener)
            MediaObject.Type.PLAYER.ordinal -> PlayerViewHolder(parent.inflate(R.layout.li_media_object_player), mOnSelectMediaObjectListener)
            MediaObject.Type.DIVIDER.ordinal -> DividerViewHolder(parent.inflate(R.layout.li_media_object_divider))
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    override fun getItemCount() = mList.size

    override fun getItemId(position: Int): Long {
        return mList[position].id
    }

    override fun getItemViewType(position: Int): Int {
        return mList[position].type.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mediaObject: MediaObject = mList[position]
        when (holder) {
            is PlayerViewHolder -> holder.bind(mediaObject, mediaObject == mSelectedItem)
            is CommonViewHolder -> holder.bind(mediaObject)
        }
    }

    class DividerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    fun show(mediaObjects: List<MediaObject>) {
        mList = mediaObjects
        notifyDataSetChanged()
    }

    fun selectItem(mediaObject: MediaObject) {
        val oldPosition = mList.indexOf(mSelectedItem)
        val newPosition = mList.indexOf(mediaObject)
        mSelectedItem = mediaObject
        notifyItemChanged(oldPosition)
        notifyItemChanged(newPosition)
    }

    interface OnSelectMediaObjectListener {
        fun onSelect(mediaObject: MediaObject)
        fun onDownload(mediaObject: MediaObject)
    }

}