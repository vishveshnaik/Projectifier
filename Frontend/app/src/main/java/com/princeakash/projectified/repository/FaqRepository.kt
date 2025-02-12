package com.princeakash.projectified.repository

import com.princeakash.projectified.service.FaqService
import com.princeakash.projectified.model.faq.BodyAddQuestion
import retrofit2.Retrofit

class FaqRepository(val retrofit: Retrofit) {

    var faqService: FaqService = retrofit.create(FaqService::class.java)

    suspend fun getAllFaq() = faqService.getAllFaq()

    suspend fun addQuestion(bodyAddQuestion: BodyAddQuestion) = faqService.addQuestion(bodyAddQuestion)
}