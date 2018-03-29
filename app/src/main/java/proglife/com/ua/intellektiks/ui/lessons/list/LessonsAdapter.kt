package proglife.com.ua.intellektiks.ui.lessons.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.li_lesson.view.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.LessonPreview

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class LessonsAdapter(private val mPresenter: LessonsPresenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mList: List<LessonPreview> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LessonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_lesson, parent, false))
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LessonViewHolder) {
            val lessonPreview = mList[position]
            holder.tvName.text = lessonPreview.name
            holder.itemView.setOnClickListener { mPresenter.openLesson(lessonPreview) }
        }
    }

    class LessonViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivCheck: ImageView = itemView.ivCheck
        val tvName: TextView = itemView.tvName
    }

    fun show(list: List<LessonPreview>) {
        mList = list
        notifyDataSetChanged()
    }

}