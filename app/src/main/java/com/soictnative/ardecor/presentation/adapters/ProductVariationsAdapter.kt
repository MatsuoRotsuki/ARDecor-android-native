package com.soictnative.ardecor.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.R
import com.soictnative.ardecor.databinding.ProductVariationsRvItemBinding

class ProductVariationsAdapter(
    private val currentProductId: Int,
    private val onClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductVariationsAdapter.ProductVariationsViewHolder>() {

    inner class ProductVariationsViewHolder(private val binding: ProductVariationsRvItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

            fun bind(product: Product) {
                binding.apply {
                    Glide.with(itemView).load(product.image_url).into(imageProductVariation)
                    if (currentProductId == product.id)
                        imageProductVariation.setBackgroundResource(R.drawable.round_rectangle_background)
                }
                itemView.setOnClickListener {
                    onClick(product)
                }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductVariationsViewHolder {
        return ProductVariationsViewHolder(
            ProductVariationsRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductVariationsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }
}