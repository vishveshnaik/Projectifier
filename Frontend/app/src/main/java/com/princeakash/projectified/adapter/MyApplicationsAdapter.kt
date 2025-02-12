package com.princeakash.projectified.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.CardMyapplicationBinding
import com.princeakash.projectified.model.candidate.ApplicationCardModelCandidate
import java.text.SimpleDateFormat

class MyApplicationsAdapter(private var applicationList : List<ApplicationCardModelCandidate>, private val listener: MyApplicationsListener): RecyclerView.Adapter<MyApplicationsAdapter.MyApplicationsViewHolder>() {

    class MyApplicationsViewHolder(val binding: CardMyapplicationBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyApplicationsViewHolder {
        val binding = CardMyapplicationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyApplicationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyApplicationsViewHolder, position: Int) {
        holder.binding.apply {
            textViewName.text = applicationList[position].recruiter_name
            textViewPost.text = applicationList[position].offer_name
            textViewCollege.text= applicationList[position].collegeName
            textViewDate.text = (SimpleDateFormat("dd MMMM yyyy HH:mm:ss z")).format(applicationList[position].float_date)
            root.setOnClickListener { listener.onViewDetailsClick(applicationList[position].application_id) }

            if(applicationList[position].is_Seen)
                imageViewSeen.setImageResource(R.drawable.ic_baseline_favorite_24)
            if(applicationList[position].is_Selected)
                imageViewSelected.setImageResource(R.drawable.ic_baseline_done_24)
        }
    }

    override fun getItemCount(): Int = applicationList.size

    interface MyApplicationsListener{
        fun onViewDetailsClick(applicationId: String)
    }
}