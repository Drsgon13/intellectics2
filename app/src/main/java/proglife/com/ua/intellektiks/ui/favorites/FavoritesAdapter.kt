package proglife.com.ua.intellektiks.ui.favorites

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.li_favorite.view.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.Favorite
import proglife.com.ua.intellektiks.utils.inflate

class FavoritesAdapter(private val list: MutableList<Favorite>, private val presenter: FavoritesPresenter): RecyclerView.Adapter<FavoritesAdapter.ItemHolder>(){
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.tvTitle.text = list[position].name

        holder.btnFavorite.setOnClickListener {
            presenter.showConfirm(list[position])
        }
        holder.itemView.setOnClickListener { presenter.openItem(list[position]) }
    }

    override fun getItemId(position: Int): Long {
        return list[position].id.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
       return ItemHolder(parent.inflate(R.layout.li_favorite))
    }

    fun removeItem(favorite: Favorite) {
        list.remove(favorite)
        notifyDataSetChanged()
    }

    class ItemHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val btnFavorite = itemView.btnFavorite!!
        val tvTitle = itemView.tvTitle!!
    }
}