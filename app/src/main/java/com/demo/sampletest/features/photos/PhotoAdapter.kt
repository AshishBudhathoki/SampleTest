package com.demo.sampletest.features.photos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sampletest.data.model.UserInfo
import com.demo.sampletest.data.model.UserPhotos
import com.demo.sampletest.databinding.UserPhotosRecyclerItemBinding
import com.demo.sampletest.features.users.UserAdapter
import com.squareup.picasso.Picasso

class PhotoAdapter(private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    private var userPhotos: List<UserPhotos> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            UserPhotosRecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoAdapter.ViewHolder, position: Int) {
        if (userPhotos.isNotEmpty()) holder.bind(userPhotos[position], itemClickListener)
    }

    override fun getItemCount(): Int = userPhotos.size

    inner class ViewHolder(private val binding: UserPhotosRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userPhotos: UserPhotos, itemClickListener: OnItemClickListener) {
            binding.apply {
                textImageDescription.text = userPhotos.title
                Picasso.get().load(userPhotos.thumbnailUrl).into(imageThumbnail);
                itemView.setOnClickListener {
                    if (adapterPosition != -1) itemClickListener.onItemClicked(
                        userPhotos.url,
                        itemView
                    )
                }
            }
        }
    }

    fun setData(data: List<UserPhotos>) {
        userPhotos = data
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClicked(userId: String, itemView: View)
    }
}