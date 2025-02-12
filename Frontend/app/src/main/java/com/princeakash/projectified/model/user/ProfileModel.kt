package com.princeakash.projectified.model.user

data class ProfileModel(
        var name:String,
        var collegeName:String,
        var course: String,
        var semester:String,
        var languages:IntArray,
        var interest1:String,
        var interest2:String?,
        var interest3:String?,
        var description:String,
        var hobbies: String
){
    constructor(bodyUpdateProfile: BodyUpdateProfile):this(
            bodyUpdateProfile.name,
            bodyUpdateProfile.collegeName,
            bodyUpdateProfile.course,
            bodyUpdateProfile.semester,
            bodyUpdateProfile.languages,
            bodyUpdateProfile.interest1,
            bodyUpdateProfile.interest2,
            bodyUpdateProfile.interest3,
            bodyUpdateProfile.description,
            bodyUpdateProfile.hobbies
    )
}