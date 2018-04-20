package proglife.com.ua.intellektiks.ui.content.holders

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.li_media_header.view.*
import kotlinx.android.synthetic.main.view_marker.view.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.Marker
import proglife.com.ua.intellektiks.ui.content.models.HeaderViewModel
import java.util.*

class HeaderViewHolder(itemView: View, private val onHeaderAction: OnHeaderAction?) : RecyclerView.ViewHolder(itemView) {

    private val tvName: TextView = itemView.tvName
    private val llMarker: LinearLayout = itemView.llMarker

    fun bind(headerViewModel: HeaderViewModel) {
        tvName.text = headerViewModel.title
        if (headerViewModel.markers != null && headerViewModel.markers!!.isNotEmpty()) {
            val markers = headerViewModel.markers!!
            if (llMarker.childCount == markers.size) return
            val inflater = LayoutInflater.from(llMarker.context)
            for (item in markers) {
                val view = inflater.inflate(R.layout.view_marker, null, false)
                val title = SpannableString(llMarker.context.getString(R.string.marker_text, item.title,
                        parseTime(item.position)))
                title.setSpan(StyleSpan(Typeface.BOLD), 19, 19 + item
                        .title.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

                view.tvText.text = title
                view.btnContinue.setOnClickListener { onHeaderAction?.onClickMarker(0, item) }
                view.btnDelete.setOnClickListener {
                    llMarker.removeView(view)
                    onHeaderAction?.onClickMarker(1, item)
                }
                view.btnNo.setOnClickListener { llMarker.removeView(view) }
                llMarker.addView(view)

            }
        } else llMarker.removeAllViews()

        llMarker.visibility = if(llMarker.childCount > 0) View.VISIBLE else View.GONE
    }

    private fun parseTime(time: Long): String{
        val totalSeconds = time / 1000
        val seconds = totalSeconds % 60
        val hours = totalSeconds / 3600 % 24
        val minutes = totalSeconds % 3600 / 60
        return String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds)
    }

    interface OnHeaderAction {
        fun onClickMarker(type: Int, marker: Marker)
    }
}