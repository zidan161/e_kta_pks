package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemCardEventBinding
import com.squareup.picasso.Picasso

class EventViewAdapter(private val listEvent: ArrayList<Event>) : RecyclerView.Adapter<EventViewAdapter.EventViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemCardEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(listEvent[position])
    }

    override fun getItemCount(): Int = listEvent.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class EventViewHolder(private val binding: ItemCardEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event){
            with(binding){
                Picasso.get().load(event.image).fit().into(imgItemPhoto)

                tvItemName.text = event.name
                tvItemDescription.text = event.desc

                itemView.setOnClickListener{ onItemClickCallback?.onItemClicked(event) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Event)
    }
}