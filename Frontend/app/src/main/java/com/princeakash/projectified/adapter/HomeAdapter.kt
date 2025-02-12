package com.princeakash.projectified.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.adapter.HomeAdapter.HomeViewHolder
import com.princeakash.projectified.databinding.CardHomeJobBinding
import java.util.*

class HomeAdapter(private val list: ArrayList<HomeItem>, private val homeListener: HomeListener) : RecyclerView.Adapter<HomeViewHolder>() {

    class HomeViewHolder(var binding: CardHomeJobBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = CardHomeJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.binding.image.setImageResource(list[position].itemDrawable)
        holder.binding.description.text = list[position].itemName
        holder.binding.root.setOnClickListener { homeListener.onDomainClick(position) }
    }

    override fun getItemCount() = list.size

    class HomeItem(var itemName: String, var itemDrawable: Int, var domainArg: String)

    interface HomeListener{
        fun onDomainClick(position:Int)
    }

    companion object{
        const val ANDROID = "android"
        const val WEB = "web"
        const val ML = "ml"
        const val AI = "ai"
        const val VIDEO = "video"
        const val UIUX = "uiux"
        const val CONTENT = "content"
        const val OTHERS = "others"
    }
}