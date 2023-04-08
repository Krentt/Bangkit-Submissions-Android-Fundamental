package com.example.mygithubuserapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygithubuserapp.api.ItemsItem
import com.example.mygithubuserapp.R
import de.hdodenhof.circleimageview.CircleImageView

class ListUserAdapter (private val listUser: List<ItemsItem>) : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imgPhoto: CircleImageView = itemView.findViewById(R.id.iv_profile)
        val tvName : TextView = itemView.findViewById(R.id.tv_nama)
    }

    interface OnItemClickCallback{
        fun onItemCLicked(data: ItemsItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nama = listUser[position].login
        val photo = listUser[position].avatarUrl
        Glide.with(holder.itemView.context)
            .load(photo)
            .into(holder.imgPhoto)
        holder.tvName.text = nama
        holder.itemView.setOnClickListener { onItemClickCallback.onItemCLicked(listUser[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listUser.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.list_user, parent, false)
        return ViewHolder(view)
    }
}