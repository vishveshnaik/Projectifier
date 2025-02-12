package com.princeakash.projectified.model.recruiter

data class ResponseGetOffersByRecruiter(
        var code: Int,
        var message:String,
        var offers:List<OfferCardModelRecruiter>?
)