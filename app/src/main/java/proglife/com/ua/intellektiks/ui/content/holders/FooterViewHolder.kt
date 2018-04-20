package proglife.com.ua.intellektiks.ui.content.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.li_footer.view.*
import proglife.com.ua.intellektiks.ui.content.models.FooterViewModel

/**
 * Created by Evhenyi Shcherbyna on 20.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class FooterViewHolder(itemView: View, private val onFooterAction: OnFooterAction?): RecyclerView.ViewHolder(itemView) {

    private val btnShowDescription: Button = itemView.btnShowDescription

    fun bind(footerViewModel: FooterViewModel) {
        if (!footerViewModel.description.isNullOrBlank()) {
            btnShowDescription.setOnClickListener { onFooterAction?.showDescription(footerViewModel.description!!) }
        }
    }

    interface OnFooterAction {
        fun showDescription(content: String)
    }

}