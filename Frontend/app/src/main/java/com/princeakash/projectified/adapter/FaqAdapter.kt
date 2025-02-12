package com.princeakash.projectified.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.databinding.CardFaqBinding
import com.princeakash.projectified.model.faq.FaqModel


class FaqAdapter(private val faqList: List<FaqModel>): RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {
    class FaqViewHolder(val binding: CardFaqBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val binding = CardFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FaqViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        holder.binding.textViewQuestion.text = faqList[position].question
        holder.binding.textViewAnswer.text = faqList[position].answer
    }

    override fun getItemCount() = faqList.size
}