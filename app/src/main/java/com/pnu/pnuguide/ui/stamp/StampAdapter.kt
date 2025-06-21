package com.pnu.pnuguide.ui.stamp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.pnu.pnuguide.R

class StampAdapter : RecyclerView.Adapter<StampAdapter.StampViewHolder>() {
    private val items = MutableList(8) { false }

    fun submitList(list: List<Boolean>) {
        for (i in items.indices) {
            items[i] = list.getOrElse(i) { false }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StampViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stamp_slot, parent, false)
        return StampViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: StampViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class StampViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image_stamp)
        fun bind(collected: Boolean) {
            image.visibility = if (collected) View.VISIBLE else View.GONE
        }
    }
}
