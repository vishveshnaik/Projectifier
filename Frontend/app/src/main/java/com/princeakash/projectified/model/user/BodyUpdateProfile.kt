package com.princeakash.projectified.model.user

data class BodyUpdateProfile (
        var name:String,
        var collegeName:String,
        var course: String,
        var semester:String,
        var languages:IntArray,
        var interest1:String,
        var interest2:String?,
        var interest3:String?,
        var description:String,
        var hobbies: String,
        var userID: String
)