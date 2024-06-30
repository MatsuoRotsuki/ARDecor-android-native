package com.soictnative.ardecor.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.soictnative.ardecor.data.dto.ProductImage
import com.soictnative.ardecor.databinding.ViewpagerImageItemBinding

class ViewPager2Images : RecyclerView.Adapter<ViewPager2Images.ViewPager2ImagesViewHolder>() {
    inner class ViewPager2ImagesViewHolder(private val binding: ViewpagerImageItemBinding):
        ViewHolder(binding.root) {
        fun bind(productImage: ProductImage) {
            Glide.with(itemView).load(productImage.image_url).into(binding.imageProductDetail)
        }

    }

    private val diffCallback = object : DiffUtil.ItemCallback<ProductImage>() {
        override fun areItemsTheSame(oldItem: ProductImage, newItem: ProductImage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductImage, newItem: ProductImage): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPager2ImagesViewHolder {
        return ViewPager2ImagesViewHolder(
            ViewpagerImageItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewPager2ImagesViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.bind(image)
    }
}