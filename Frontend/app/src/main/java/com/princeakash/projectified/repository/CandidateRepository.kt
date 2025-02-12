
package com.princeakash.projectified.repository

import com.princeakash.projectified.service.CandidateService
import com.princeakash.projectified.model.candidate.BodyAddApplication
import com.princeakash.projectified.model.candidate.BodyUpdateApplication
import retrofit2.Retrofit

//TODO:Show exception via toast
class CandidateRepository(retrofit: Retrofit) {
    var candidateService: CandidateService = retrofit.create(CandidateService::class.java)

    suspend fun addApplication(token: String, bodyAddApplication: BodyAddApplication) = candidateService.addApplication(token, bodyAddApplication)

    suspend fun getApplicationByCandidate(token: String, applicantID: String) = candidateService.getApplicationsByCandidate(token, applicantID)

    suspend fun getApplicationDetailByIdCandidate(token: String, applicationID: String) = candidateService.getApplicationByIdCandidate(token, applicationID)

    suspend fun updateApplication(token: String, applicationID: String, bodyUpdateApplication: BodyUpdateApplication) = candidateService.updateApplication(token, applicationID, bodyUpdateApplication)

    suspend fun deleteApplication(token: String, applicationID: String) = candidateService.deleteApplication(token, applicationID)

    suspend fun getOffersByDomain(token: String, domainName: String) = candidateService.getOffersByDomain(token, domainName)

    suspend fun getOfferById(token: String, offerId: String) = candidateService.getOfferById(token, offerId)


}