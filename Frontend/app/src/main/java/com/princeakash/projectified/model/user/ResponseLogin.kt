package com.princeakash.projectified.model.user

data class ResponseLogin(
        var code: Int,
        var message: String,
        var _id: String?,
        var token: String?,
        var profileCompleted: Boolean?,
        //var profile: ProfileModel?
        var name:String?,
        var collegeName:String?,
        var course: String?,
        var semester:String?,
        var languages:IntArray?,
        var interest1:String?,
        var interest2:String?,
        var interest3:String?,
        var description:String?,
        var hobbies: String?
)