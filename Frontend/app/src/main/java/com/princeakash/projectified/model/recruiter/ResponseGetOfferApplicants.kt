package com.princeakash.projectified.model.recruiter

data class ResponseGetOfferApplicants (
        var code: Int,
        var message: String,
        var applicants: List<ApplicantCardModel>?
)