package com.soictnative.ardecor.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.databinding.FurnitureInventoryProductRvItemBinding

class FurnitureInventoryProductAdapter : RecyclerView.Adapter<FurnitureInventoryProductAdapter.FurnitureInventoryViewHolder>() {

    inner class FurnitureInventoryViewHolder(private val binding: FurnitureInventoryProductRvItemBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(product: Product) {
                Glide.with(itemView).load(product.image_url).into(binding.imageProductFurnitureInventory)
                binding.tvProductNameFurnitureInventory.text = product.name
                binding.tvProductPriceFurnitureInventory.text = String.format("$%.2f", product.price)
            }
        }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FurnitureInventoryViewHolder {
        return FurnitureInventoryViewHolder(
            FurnitureInventoryProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FurnitureInventoryViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    var onClick: ((Product) -> Unit)? = null
}