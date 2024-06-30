package com.soictnative.ardecor.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.databinding.FurnitureContextItemBinding

class FurnitureContextAdapter : RecyclerView.Adapter<FurnitureContextAdapter.FurnitureContextViewHolder>() {
    inner class FurnitureContextViewHolder(private val binding: FurnitureContextItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(product: Product)
            {
                binding.apply {
                    Glide.with(itemView).load(product.image_url).into(imageFurnitureContextRvItem)
                    tvProductNameFurnitureContext.text = product.name
                    tvProductPriceFurnitureContext.text = String.format("$%.2f", product.price)
                }
                itemView.setOnClickListener { onClick?.invoke(product) }
            }
        }

    var onClick: ((Product) -> Unit)? = null

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FurnitureContextViewHolder {
        return FurnitureContextViewHolder(
            FurnitureContextItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FurnitureContextViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }
}