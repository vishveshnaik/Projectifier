package com.princeakash.projectified.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.databinding.CardMyOffersBinding
import com.princeakash.projectified.model.recruiter.OfferCardModelRecruiter
import java.text.SimpleDateFormat

class MyOffersAdapter(private var offerList: List<OfferCardModelRecruiter>, private val listener: MyOffersListener): RecyclerView.Adapter<MyOffersAdapter.MyOffersViewHolder>() {

    class MyOffersViewHolder(val binding: CardMyOffersBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOffersViewHolder {
        val binding = CardMyOffersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyOffersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyOffersViewHolder, position: Int) {
        holder.binding.apply {
            textViewPost.text = offerList[position].offer_name
            textViewDate.text = (SimpleDateFormat("dd MMMM yyyy HH:mm:ss z")).format(offerList[position].float_date)
            textViewCollege.text =  "${offerList[position].no_of_applicants} Applicants"
            root.setOnClickListener { listener.onViewDetailsClick(offerList[position].offer_id) }
        }
    }

    override fun getItemCount(): Int = offerList.size

    interface MyOffersListener{
        fun onViewDetailsClick(offerId: String)
    }
}