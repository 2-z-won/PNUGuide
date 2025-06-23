package com.pnu.pnuguide.ui.stamp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pnu.pnuguide.data.Stamp
import com.pnu.pnuguide.databinding.ItemStampRowBinding

class StampAdapter(
    private val onVerify: (Stamp) -> Unit
) : ListAdapter<Stamp, StampAdapter.StampViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StampViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStampRowBinding.inflate(inflater, parent, false)
        return StampViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StampViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StampViewHolder(private val binding: ItemStampRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Stamp) {
            binding.textName.text = item.name
            binding.buttonVerify.setOnClickListener { onVerify(item) }
            binding.textName.alpha = if (item.collected) 0.5f else 1f
            binding.buttonVerify.isEnabled = !item.collected
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Stamp>() {
        override fun areItemsTheSame(oldItem: Stamp, newItem: Stamp): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Stamp, newItem: Stamp): Boolean = oldItem == newItem
    }
}
