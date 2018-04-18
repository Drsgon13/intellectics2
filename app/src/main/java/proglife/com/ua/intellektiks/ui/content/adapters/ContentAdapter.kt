package proglife.com.ua.intellektiks.ui.content.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.MediaObject
import proglife.com.ua.intellektiks.data.models.ReportMessage
import proglife.com.ua.intellektiks.ui.content.holders.*
import proglife.com.ua.intellektiks.ui.content.models.HeaderViewModel
import proglife.com.ua.intellektiks.ui.content.models.PlayerViewModel
import proglife.com.ua.intellektiks.ui.content.models.ReportsViewModel
import proglife.com.ua.intellektiks.utils.inflate

/**
 * Created by Evhenyi Shcherbyna on 17.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class ContentAdapter(
        private val onHeaderAction: HeaderViewHolder.OnHeaderAction? = null,
        private val onReportAction: ReportsViewHolder.OnReportAction? = null,
        private val onMediaObjectAction: OnMediaObjectAction? = null
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mContext: Context
    private var mHeaderViewModel = HeaderViewModel()
    private var mPlayerViewModel = PlayerViewModel()
    private var mReportsViewModel = ReportsViewModel()
    private var mList: List<MediaObject> = emptyList()
    private var mSelectedItem: MediaObject? = null

    companion object {
        private const val HEADER = 6000
        private const val PLAYER = 6200
        private const val REPORTS = 7000
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mContext = recyclerView.context
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount(): Int {
        return getHeaderSize() + mList.size + getFooterSize()
    }

    // +1 заголовок отображается всегда
    private fun getHeaderSize() = if (mPlayerViewModel.show) 2 else 1

    private fun getFooterSize() = if (mReportsViewModel.show) 1 else 0

    override fun getItemViewType(position: Int): Int {
        return when {
            position == getHeaderPosition() -> HEADER
            mPlayerViewModel.show && position == getPlayerPosition() -> PLAYER
            mReportsViewModel.show && position == getFooterPosition() -> REPORTS
            else -> mList[position - getHeaderSize()].type.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> HeaderViewHolder(parent.inflate(R.layout.li_media_header), onHeaderAction)
            PLAYER -> PlayerViewHolder(parent.inflate(R.layout.li_player))
            REPORTS -> ReportsViewHolder(parent.inflate(R.layout.li_lesson_footer), onReportAction)
            MediaObject.Type.COMMON.ordinal -> CommonItemViewHolder(parent.inflate(R.layout.li_media_object_common), onMediaObjectAction)
            MediaObject.Type.PLAYER.ordinal -> PlayerItemViewHolder(parent.inflate(R.layout.li_media_object_player), onMediaObjectAction)
            MediaObject.Type.DIVIDER.ordinal -> DividerViewHolder(parent.inflate(R.layout.li_media_object_divider))
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ReportsViewHolder -> holder.bind(mReportsViewModel)
            is HeaderViewHolder -> holder.bind(mHeaderViewModel)
            is PlayerViewHolder -> holder.bind(mPlayerViewModel)
            is PlayerItemViewHolder -> holder.bind(mList[position - getHeaderSize()], mList[position - getHeaderSize()] == mSelectedItem)
            is CommonItemViewHolder -> holder.bind(mList[position - getHeaderSize()])
        }
    }

    private fun getFooterPosition() = itemCount - 1

    private fun getHeaderPosition() = 0

    fun getPlayerPosition() = 1

    fun showHeader(init: HeaderViewModel.() -> Unit) {
        init(mHeaderViewModel)
        notifyItemChanged(getHeaderPosition())
    }

    fun showReports(show: Boolean, messages: List<ReportMessage>) {
        val reportsViewModel = ReportsViewModel(show, messages)
        val needRecount = mReportsViewModel.show != reportsViewModel.show
        mReportsViewModel = reportsViewModel
        if (needRecount) notifyDataSetChanged() else notifyItemChanged(getFooterPosition())
    }

    interface OnMediaObjectAction {
        fun onSelect(mediaObject: MediaObject)
        fun onDownload(mediaObject: MediaObject)
    }

    fun showMedia(mediaObjects: List<MediaObject>) {
        mList = mediaObjects
        notifyDataSetChanged()
    }

    fun selectItem(mediaObject: MediaObject) {
        val oldPosition = mList.indexOf(mSelectedItem)
        val newPosition = mList.indexOf(mediaObject)
        mSelectedItem = mediaObject
        notifyItemChanged(oldPosition + getHeaderSize())
        notifyItemChanged(newPosition + getHeaderSize())
    }

    fun notifyMediaItemChanged(index: Int) {
        notifyItemChanged(getHeaderSize() + index)
    }

    fun showPlayerView(playerView: PlayerView) {
        mPlayerViewModel.playerView = playerView
        if (!mPlayerViewModel.show) {
            mPlayerViewModel.show = true
            notifyDataSetChanged()
        } else {
            notifyItemChanged(getPlayerPosition())
        }

    }

    fun removePlayerView() {
        mPlayerViewModel.playerView = null
        notifyItemChanged(getPlayerPosition())
    }

}