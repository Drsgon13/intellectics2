package proglife.com.ua.intellektiks.ui.base.media

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import kotlinx.android.synthetic.main.view_marker.view.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.Marker
import java.util.*

class MarkerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val context = itemView.context
    private val tvText = itemView.tvText
    private val btnContinue = itemView.btnContinue
    private val btnDelete = itemView.btnDelete
    private val btnNo = itemView.btnNo

    fun bind(marker: Marker, onClickMarker: MarkerAdapter.OnClickMarker){

        val string = SpannableString(context.getString(R.string.marker_text, marker.title,
                parseTime(marker.position)))
        string.setSpan(StyleSpan(Typeface.BOLD), 19, 19 + marker
                .title.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        tvText.text = string

        btnContinue.setOnClickListener{ onClickMarker.onContinue(marker)}
        btnDelete.setOnClickListener{ onClickMarker.onDelete(marker)}
        btnNo.setOnClickListener{ onClickMarker.onNo(marker)}
    }

    private fun parseTime(time: Long): String{
        val totalSeconds = time / 1000
        val seconds = totalSeconds % 60
        val hours = totalSeconds / 3600 % 24
        val minutes = totalSeconds % 3600 / 60
        return String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds)
    }
}