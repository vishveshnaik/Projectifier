package com.princeakash.projectified.model.recruiter

import java.io.Serializable

data class ResponseGetOfferByIdRecruiter(
        var code: Int,
        var message:String,
        var offer: Offer?
) : Serializable
{
    data class Offer(
            var offer_id:String,
            var offer_name:String,
            var requirements:String,
            var skills:String,
            var expectation: String,
            var is_visible:Boolean
    )
}