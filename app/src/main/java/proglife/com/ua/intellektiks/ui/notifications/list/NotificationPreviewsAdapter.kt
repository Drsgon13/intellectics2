package proglife.com.ua.intellektiks.ui.notifications.list

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.li_notification_preview.view.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.NotificationMessagePreview
import proglife.com.ua.intellektiks.utils.inflate
import java.text.SimpleDateFormat
import android.text.Spannable
import android.icu.lang.UProperty.INT_START
import android.text.SpannableStringBuilder



/**
 * Created by Evhenyi Shcherbyna on 07.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class NotificationPreviewsAdapter(private val presenter: NotificationListPresenter):
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mList = emptyList<NotificationMessagePreview>()
    @SuppressLint("SimpleDateFormat")
    private val mSDF = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotificationPreviewViewHolder(parent.inflate(R.layout.li_notification_preview))
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mList[position]
        if (holder is NotificationPreviewViewHolder) {
            holder.tvDate.text = mSDF.format(item.sentDate)

            val str = SpannableStringBuilder(item.subject)
            if(item.productive == "0")
            str.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, item.subject.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            holder.tvTitle.text = str

            holder.itemView.setOnClickListener {
                mList[position].productive = "1"
                presenter.openNotification(item) }
        }
    }

    fun show(list: List<NotificationMessagePreview>) {
        mList = list
        notifyDataSetChanged()
    }

    class NotificationPreviewViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.tvDate
        val tvTitle: TextView = itemView.tvTitle
    }

}