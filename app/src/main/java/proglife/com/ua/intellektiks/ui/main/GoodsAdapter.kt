package proglife.com.ua.intellektiks.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.li_goods.view.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.GoodsPreview

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class GoodsAdapter(private val mPresenter: MainPresenter): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mList: List<GoodsPreview> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GoodsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_goods, parent, false))
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GoodsViewHolder) {
            val goods = mList[position]
            Glide.with(holder.itemView)
                    .load(goods.image)
                    .into(holder.ivImage)
            holder.tvName.text = goods.name
            holder.itemView.setOnClickListener { mPresenter.selectGoods(goods) }
        }
    }

    class GoodsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.ivImage
        val tvName: TextView = itemView.tvName
    }

    fun show(list: List<GoodsPreview>) {
        mList = list
        notifyDataSetChanged()
    }

}