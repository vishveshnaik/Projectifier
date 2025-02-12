package com.princeakash.projectified.model.user

data class ResponseGetProfileById(
        var code: Int,
        var message: String,
        var id:String?,
        var name:String?,
        var date:String?,
        var collegeName:String?,
        var semester:String?,
        var languages:String?,
        var interest1:String?,
        var interest2:String?,
        var interest3:String?,
        var description:String?
)