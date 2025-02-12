package com.princeakash.projectified.model.candidate

data class ResponseGetOfferById(
        val code: Int,
        val message: String,
        val offer: OfferCandidate
)