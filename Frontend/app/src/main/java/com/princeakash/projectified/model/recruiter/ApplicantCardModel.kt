package com.princeakash.projectified.model.recruiter

import java.util.*

data class ApplicantCardModel (
        var application_id:String,
        var collegeName:String,
        var is_Seen:Boolean,
        var is_Selected:Boolean,
        var date:Date,
        var applicant_id:String
)