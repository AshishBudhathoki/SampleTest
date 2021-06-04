package com.demo.sampletest.features.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sampletest.data.model.UserInfo
import com.demo.sampletest.databinding.UsersRecyclerItemBinding

class UserAdapter(private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var launchesList: List<UserInfo> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            UsersRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (launchesList.isNotEmpty()) holder.bind(launchesList[position], itemClickListener)
    }

    override fun getItemCount(): Int = launchesList.size

    inner class ViewHolder(private val binding: UsersRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserInfo, itemClickListener: OnItemClickListener) {
            binding.apply {
                textUserName.text = user.username
                textUserPhone.text = user.phone
                textUserEmail.text = user.email
                itemView.setOnClickListener {
                    if (adapterPosition != -1) itemClickListener.onItemClicked(
                        user.id,
                        itemView
                    )
                }
            }
        }
    }

    fun setData(data: List<UserInfo>) {
        launchesList = data
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClicked(userId: Int, itemView: View)
    }
}