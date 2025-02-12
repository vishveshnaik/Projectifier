package com.princeakash.projectified.service

import com.princeakash.projectified.model.user.*
import retrofit2.http.*

interface ProfileService {

    @POST("user/signup")
    suspend fun signUp(@Body bodySignUp: BodySignUp): ResponseSignUp

    @POST("user/login")
    suspend fun logIn(@Body bodyLogin: BodyLogin): ResponseLogin

    @PATCH("user/{profileID}")
    suspend fun updateProfile(@Header("Authorization") token: String, @Body bodyUpdateProfile: BodyUpdateProfile, @Path("profileID") profileID:String) : ResponseUpdateProfile

    @POST("user/checksignup")
    suspend fun checkSignup(@Body bodySignUp: BodySignUp) : ResponseSignUp

    @POST("user/reset/generateOtp")
    suspend fun generateOtp(@Body bodyGenerateOtp: BodyGenerateOtp): ResponseGenerateOtp

    @POST("user/reset/verifyOtp")
    suspend fun verifyOtp(@Body bodyVerifyOtp: BodyVerifyOtp): ResponseVerifyOtp

    @POST("user/reset/updatePassword")
    suspend fun updatePassword(@Body bodyUpdatePassword: BodyUpdatePassword): ResponseUpdatePassword
}