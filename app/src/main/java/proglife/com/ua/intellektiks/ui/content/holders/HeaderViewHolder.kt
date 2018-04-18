package proglife.com.ua.intellektiks.ui.content.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.li_media_header.view.*
import proglife.com.ua.intellektiks.ui.content.models.HeaderViewModel

class HeaderViewHolder(itemView: View, private val onHeaderAction: OnHeaderAction?) : RecyclerView.ViewHolder(itemView) {

    private val tvName: TextView = itemView.tvName
    private val rvMarker: RecyclerView = itemView.rvMarker
    private val btnShowDescription: Button = itemView.btnShowDescription

    fun bind(headerViewModel: HeaderViewModel) {
        tvName.text = headerViewModel.title
        if (!headerViewModel.description.isNullOrBlank()) {
            btnShowDescription.visibility = View.VISIBLE
            btnShowDescription.setOnClickListener { onHeaderAction?.showDescription(headerViewModel.description!!) }
        }
    }

    interface OnHeaderAction {
        fun showDescription(content: String)
    }

}