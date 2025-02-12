package com.princeakash.projectified.model.candidate

import java.util.*

data class BodyAddApplication (
        var apply_date: Date,
        var resume: String,
        var previousWork: String,
        var applicant_id:String,
        var offer_id:String,
)