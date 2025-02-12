package com.princeakash.projectified.model.candidate

import java.util.*

data class ApplicationCardModelCandidate (
        var recruiter_name: String,
        var  offer_name: String,
        var  application_id: String,
        var  collegeName:String,
        var float_date: Date,
        var is_Seen : Boolean,
        var is_Selected:Boolean
)