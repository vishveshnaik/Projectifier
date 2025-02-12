package com.princeakash.projectified.model.candidate

data class ResponseGetOffersByDomain(
        val code: String,
        val message: String,
        val offers: List<OfferCardModelCandidate>?
)