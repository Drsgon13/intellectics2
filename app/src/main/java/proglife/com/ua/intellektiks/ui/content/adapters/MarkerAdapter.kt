package proglife.com.ua.intellektiks.ui.content.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.Marker
import proglife.com.ua.intellektiks.ui.content.holders.MarkerItemViewHolder
import proglife.com.ua.intellektiks.utils.inflate

class MarkerAdapter(private val list: MutableList<Marker>, private val onClickMarker: OnClickMarker): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
      return MarkerItemViewHolder(parent.inflate(R.layout.view_marker))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? MarkerItemViewHolder)?.bind(list[position], onClickMarker)
    }

    fun hide(marker: Marker) {
        list.remove(marker)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return list[position].mediaobjectid
    }

    interface OnClickMarker{
        fun onDelete(marker: Marker)
        fun onContinue(marker: Marker)
        fun onNo(marker: Marker)
    }
}