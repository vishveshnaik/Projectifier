package com.princeakash.projectified.service

import com.princeakash.projectified.model.candidate.*
import retrofit2.http.*
import com.princeakash.projectified.model.candidate.ResponseGetOfferById as ResponseGetOfferById1

interface CandidateService {

    // ADD Application
    @POST("application")
    suspend  fun addApplication(@Header("Authorization") token: String, @Body bodyAddApplication: BodyAddApplication) : ResponseAddApplication


    // Get all Applications by ApplicantID
    @GET("application/byApplicant/{applicantID}")
    suspend  fun getApplicationsByCandidate(@Header("Authorization") token: String, @Path("applicantID") applicantID:String) : ResponseGetApplicationsByCandidate

    // Get info about specific application by ApplicationID
    @GET("application/{applicationID}")
    suspend fun getApplicationByIdCandidate(@Header("Authorization") token: String, @Path("applicationID") applicationID:String) : ResponseGetApplicationDetailByIdCandidate


    //Update any offer through ApplicationId
    @PATCH("application/{applicationID}")
    suspend  fun  updateApplication(@Header("Authorization") token: String, @Path("applicationID") applicationID:String, @Body bodyUpdateApplication: BodyUpdateApplication): ResponseUpdateApplication


    // Delete any Application through ApplicationID
    @DELETE("application/{applicationID}")
    suspend  fun deleteApplication(@Header("Authorization") token: String, @Path("applicationID") applicationID: String): ResponseDeleteApplication


    // Get info about all offers through domain name
    @GET("offer/{domainName}")
    suspend  fun  getOffersByDomain(@Header("Authorization") token: String, @Path("domainName") domainName:String): ResponseGetOffersByDomain


    // Get Info about specific offer through offerID
    @GET("offer/{offerID}/candidate")
    suspend  fun getOfferById(@Header("Authorization") token: String, @Path("offerID") offerID:String): ResponseGetOfferById1


}



