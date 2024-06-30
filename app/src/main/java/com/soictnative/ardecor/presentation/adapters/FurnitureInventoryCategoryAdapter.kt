package com.soictnative.ardecor.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soictnative.ardecor.data.dto.Category
import com.soictnative.ardecor.databinding.FurnitureInventoryCategoryRvItemBinding

class FurnitureInventoryCategoryAdapter : RecyclerView.Adapter<FurnitureInventoryCategoryAdapter.FurnitureInventoryViewHolder>(){

    inner class FurnitureInventoryViewHolder(private val binding: FurnitureInventoryCategoryRvItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            Glide.with(itemView).load(category.image_url).into(binding.imageFurnitureInventory)
            binding.tvCategoryName.text = category.name
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FurnitureInventoryViewHolder {
        return FurnitureInventoryViewHolder(
            FurnitureInventoryCategoryRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FurnitureInventoryViewHolder, position: Int) {
        val category = differ.currentList[position]
        holder.bind(category)

        holder.itemView.setOnClickListener {
            onClick?.invoke(category)
        }
    }

    var onClick: ((Category) -> Unit)? = null
}