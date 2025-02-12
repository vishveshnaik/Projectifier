package com.princeakash.projectified.model.candidate

data class ApplicationCandidateVersion(
        //var application_id:String,
        var  requirements: String,
        var  skills:String,
        var expectation:String,
        var recruiter_name: String,
        var recruiter_collegeName:String,
        var recruiter_course:String ,
        var recruiter_semester:String,
        var recruiter_phone: String,
        var previousWork: String,
        var is_Seen:Boolean,
        var is_Selected:Boolean,
        var resume: String
)