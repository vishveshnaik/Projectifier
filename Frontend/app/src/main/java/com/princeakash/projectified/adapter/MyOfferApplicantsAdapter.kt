package com.princeakash.projectified.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.CardMyOffersCandidateBinding
import com.princeakash.projectified.model.recruiter.ApplicantCardModel
import com.princeakash.projectified.viewmodel.RecruiterCandidateViewModel

class MyOfferApplicantsAdapter(private var applicantList: List<ApplicantCardModel>, private val listener: MyOfferApplicantListener) : RecyclerView.Adapter<MyOfferApplicantsAdapter.MyOfferApplicantViewHolder>(){

    class MyOfferApplicantViewHolder(val binding: CardMyOffersCandidateBinding) : RecyclerView.ViewHolder(binding.root)

    /*private val diffUtil = object : DiffUtil.ItemCallback<ApplicantCardModel>() {
        override fun areItemsTheSame(oldItem: ApplicantCardModel, newItem: ApplicantCardModel)
            = (oldItem.application_id == newItem.application_id)

        override fun areContentsTheSame(oldItem: ApplicantCardModel, newItem: ApplicantCardModel)
            = (oldItem.is_Seen == newItem.is_Seen)&&(oldItem.is_Selected == newItem.is_Selected)
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var applicantList: List<ApplicantCardModel>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOfferApplicantViewHolder {
        val binding = CardMyOffersCandidateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyOfferApplicantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyOfferApplicantViewHolder, position: Int) {
        holder.binding.apply {
            textViewCollege.text = applicantList[position].collegeName
            textViewDate.text = applicantList[position].date.toString()
            if(applicantList[position].is_Seen)
                imageViewSeen.setImageResource(R.drawable.ic_baseline_favorite_24)
            if(applicantList[position].is_Selected)
                imageViewSelected.setImageResource(R.drawable.ic_baseline_done_24)

            imageViewSeen.setOnClickListener {
                listener.onUpdateClick(applicantList[position].application_id,
                    RecruiterCandidateViewModel.UpdateType.SEEN, applicantList[position].is_Seen)
            }
            imageViewSelected.setOnClickListener {
                listener.onUpdateClick(applicantList[position].application_id,
                    RecruiterCandidateViewModel.UpdateType.SELECTED, applicantList[position].is_Selected)
            }
            root.setOnClickListener {
                listener.onViewDetailsClick(applicantList[position].application_id)
            }
        }
    }

    override fun getItemCount() = applicantList.size

    interface MyOfferApplicantListener{
        fun onViewDetailsClick(applicationId: String)
        fun onUpdateClick(applicationId: String, type: RecruiterCandidateViewModel.UpdateType, currentStatus: Boolean)
    }
}