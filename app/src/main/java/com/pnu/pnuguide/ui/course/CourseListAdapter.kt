package com.pnu.pnuguide.ui.course

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pnu.pnuguide.R

data class CourseItem(
    val title: String,
    val duration: String,
    val imageUrl: String? = null,
    val imageRes: Int? = null
)

class CourseListAdapter : RecyclerView.Adapter<CourseListAdapter.CourseViewHolder>() {
    private val items = mutableListOf<CourseItem>()
    var onItemClick: ((CourseItem) -> Unit)? = null

    fun submitList(list: List<CourseItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = items.size

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image_course)
        private val title: TextView = itemView.findViewById(R.id.text_course_title)
        private val desc: TextView = itemView.findViewById(R.id.text_course_desc)

        fun bind(item: CourseItem) {
            // Clear any previous request to avoid showing wrong images when views are reused
            // Clear any previous Glide request to avoid showing an old image
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
