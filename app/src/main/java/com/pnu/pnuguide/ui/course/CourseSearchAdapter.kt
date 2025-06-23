package com.pnu.pnuguide.ui.course

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pnu.pnuguide.R
import androidx.recyclerview.widget.RecyclerView

class CourseSearchAdapter : RecyclerView.Adapter<CourseSearchAdapter.ViewHolder>() {
    private val items = mutableListOf<CourseItem>()
    var onItemClick: ((CourseItem) -> Unit)? = null

    fun submitItems(list: List<CourseItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun appendItems(list: List<CourseItem>) {
        items.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick?.invoke(item) }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image_course)
        private val title: TextView = itemView.findViewById(R.id.text_course_title)
        private val desc: TextView = itemView.findViewById(R.id.text_course_desc)
        fun bind(item: CourseItem) {
            // Clear previous Glide request to avoid incorrect image mapping
            // Clear previous Glide request so recycled views don't show wrong images
            Glide.with(itemView).clear(image)
            image.setImageDrawable(null)

            if (item.imageRes != null) {
                image.setImageResource(item.imageRes)
            } else if (item.imageUrl != null) {
                Glide.with(itemView).load(item.imageUrl).into(image)
            }
            title.text = item.title
            desc.text = item.duration
        }
    }
}
