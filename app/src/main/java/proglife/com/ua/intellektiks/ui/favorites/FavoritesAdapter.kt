package proglife.com.ua.intellektiks.ui.favorites

import android.support.v7.widget.RecyclerView
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.Favorite
import proglife.com.ua.intellektiks.utils.inflate

class FavoritesAdapter(private val list: List<Favorite>): RecyclerView.Adapter<FavoritesAdapter.ItemHolder>(){
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.tvTitle.text = list[position].name

        holder.itemView.setOnClickListener { it.isSelected = !it.isSelected }
    }

    override fun getItemId(position: Int): Long {
        return list[position].id.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
       return ItemHolder(parent.inflate(R.layout.li_favorite))
    }

    class ItemHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val tvTitle = itemView as TextView
    }
}