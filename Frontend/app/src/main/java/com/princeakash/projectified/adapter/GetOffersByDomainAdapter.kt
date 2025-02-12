package com.princeakash.projectified.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.databinding.CardOfferCandidateVersionBinding
import com.princeakash.projectified.model.candidate.OfferCardModelCandidate
import java.text.SimpleDateFormat

class GetOffersByDomainAdapter(private val offerList: List<OfferCardModelCandidate>, private val listener: GetOffersListener): RecyclerView.Adapter<GetOffersByDomainAdapter.GetOfferViewHolder>() {

    class GetOfferViewHolder(val binding: CardOfferCandidateVersionBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetOfferViewHolder {
        val binding = CardOfferCandidateVersionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GetOfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GetOfferViewHolder, position: Int) {
        holder.binding.apply {
            textViewPost.text = offerList[position].offer_name
            textViewDate.text = (SimpleDateFormat("dd MMMM yyyy HH:mm:ss z")).format(offerList[position].float_date)
            textViewCollege.text = offerList[position].collegeName
            textViewSkills.text = offerList[position].skills
            root.setOnClickListener { listener.onViewDetailsClick(offerList[position].offer_id) }
        }
    }

    override fun getItemCount(): Int = offerList.size

    interface GetOffersListener{
        fun onViewDetailsClick(offerId: String)
    }
}