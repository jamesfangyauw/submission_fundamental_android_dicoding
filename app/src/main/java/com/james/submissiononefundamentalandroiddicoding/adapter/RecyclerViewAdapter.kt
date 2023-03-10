package com.james.submissiononefundamentalandroiddicoding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.james.submissiononefundamentalandroiddicoding.databinding.ItemUserBinding
import com.james.submissiononefundamentalandroiddicoding.model.ItemsItem

class RecyclerViewAdapter (private val listItem : List<ItemsItem>): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private lateinit var itemUserBinding: ItemUserBinding

    class ViewHolder(var itemUserBinding : ItemUserBinding) : RecyclerView.ViewHolder(itemUserBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        itemUserBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemUserBinding)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (username, photo) = listItem[position]
        Glide.with(holder.itemView.context)
            .load(photo)
            .circleCrop()
            .into(holder.itemUserBinding.imgItemPhoto)
        holder.itemUserBinding.tvItemUsername.text = username
    }
}