package com.princeakash.projectified.model.candidate

data class ResponseGetApplicationDetailByIdCandidate (
        var code: Int,
        var message : String,
        var application: ApplicationCandidateVersion?
)

