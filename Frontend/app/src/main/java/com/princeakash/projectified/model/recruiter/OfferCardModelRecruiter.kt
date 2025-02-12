package com.princeakash.projectified.model.recruiter

import java.util.*

data class OfferCardModelRecruiter(
        var offer_id:String,
        var offer_name:String,
        var float_date:Date,
        var no_of_applicants:Int
)