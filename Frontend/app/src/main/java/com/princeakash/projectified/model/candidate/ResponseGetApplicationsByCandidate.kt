package com.princeakash.projectified.model.candidate


data class ResponseGetApplicationsByCandidate(
        var code: Int,
        var message:String,
        var applications:List<ApplicationCardModelCandidate>?
)