package com.princeakash.projectified.service

import com.princeakash.projectified.model.recruiter.*
import com.princeakash.projectified.model.recruiter.BodyAddOffer
import com.princeakash.projectified.model.recruiter.ResponseAddOffer
import retrofit2.http.*


interface RecruiterService {

    //Add Offer via Offer Body
    @POST("offer")
    suspend fun addOffer(@Header("Authorization") token: String, @Body bodyAddOffer: BodyAddOffer) : ResponseAddOffer

    //Get Offers floated by a Recruiter using RecruiterID
    @GET("offer/recruiter/{recruiterID}")
    suspend fun getOffersByRecruiter(@Header("Authorization") token: String, @Path("recruiterID") recruiterID:String) : ResponseGetOffersByRecruiter

    //Get Offer Details of Offer floated by a Recruiter using OfferID
    @GET("offer/{offerID}/recruiter")
    suspend fun getOfferByIdRecruiter(@Header("Authorization") token: String, @Path("offerID") offerID:String) : ResponseGetOfferByIdRecruiter

    //Get list of Applicants of an Offer using Offer ID
    @GET("offer/{offerID}/applicants")
    suspend fun getOfferApplicants(@Header("Authorization") token: String, @Path("offerID") offerID:String) : ResponseGetOfferApplicants

    //Update Offer via Offer ID and Body
    @PATCH("offer/{offerID}")
    suspend fun updateOffer(@Header("Authorization") token: String, @Path("offerID") offerID: String, @Body bodyUpdateOffer: BodyUpdateOffer): ResponseUpdateOffer

    //Toggle Visibility of Offer (visible/invisible) via Offer ID and Body
    @POST("offer/{offerID}/toggle")
    suspend fun toggleVisibility(@Header("Authorization") token: String, @Path("offerID") offerID: String, @Body bodyToggleVisibility: BodyToggleVisibility) : ResponseToggleVisibility

    //Delete offer using Offer ID
    @DELETE("offer/{offerID}")
    suspend fun deleteOffer(@Header("Authorization") token: String, @Path("offerID") offerID:String): ResponseDeleteOffer

    //Get Application Details using ApplicationID for Recruiter
    @GET("application/{applicationID}/recruiter")
    suspend fun getApplicationById(@Header("Authorization") token: String, @Path("applicationID") applicationID:String): ResponseGetApplicationByIdRecruiter

    //Mark an Application as Seen using ApplicationID and Body
    @PATCH("application/{applicationID}/seen")
    suspend fun markSeen(@Header("Authorization") token: String, @Path("applicationID") applicationID: String, @Body bodyMarkAsSeen: BodyMarkAsSeen): ResponseMarkAsSeen

    //Mark an Application as Selected using ApplicationID and Body
    @PATCH("application/{applicationID}/selected")
    suspend fun markSelected(@Header("Authorization") token: String, @Path("applicationID") applicationID: String, @Body bodyMarkAsSelected: BodyMarkAsSelected): ResponseMarkAsSelected

}