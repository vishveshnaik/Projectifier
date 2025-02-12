package com.princeakash.projectified.model.faq

data class ResponseGetFaq(
        val code: Int,
        val message: String,
        val faqList: List<FaqModel>
)