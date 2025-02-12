package com.princeakash.projectified.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.utils.Event
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.MyApplication.Companion.handleError
import com.princeakash.projectified.repository.CandidateRepository
import com.princeakash.projectified.model.candidate.*
import com.princeakash.projectified.model.recruiter.*
import com.princeakash.projectified.model.recruiter.BodyAddOffer
import com.princeakash.projectified.model.recruiter.ResponseAddOffer
import com.princeakash.projectified.repository.RecruiterRepository
import com.princeakash.projectified.repository.ProfileRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class RecruiterCandidateViewModel(val app: Application) : AndroidViewModel(app) {

    //RecruiterRepository instance, guaranteed to be singular because of being
    private val recruiterRepository: RecruiterRepository = (app as MyApplication).recruiterRepository
    private val profileRepository: ProfileRepository = (app as MyApplication).profileRepository
    private val candidateRepository: CandidateRepository = (app as MyApplication).candidateRepository

    //MutableLiveData variables of responses for all kinds of requests
    //which can be put to observation in Activities/Fragments
    private val responseAddOffer : MutableLiveData<Event<ResponseAddOffer>> = MutableLiveData()
    private var responseGetOffersByRecruiter: MutableLiveData<ResponseGetOffersByRecruiter> = MutableLiveData()
    private var responseGetOfferByIdRecruiter: MutableLiveData<Event<ResponseGetOfferByIdRecruiter>> = MutableLiveData()
    private var responseGetOfferApplicants: MutableLiveData<Event<ResponseGetOfferApplicants>> = MutableLiveData()
    private var responseUpdateOffer: MutableLiveData<Event<ResponseUpdateOffer>> = MutableLiveData()
    private var responseToggleVisibility: MutableLiveData<Event<ResponseToggleVisibility>> = MutableLiveData()
    private var responseDeleteOffer: MutableLiveData<Event<ResponseDeleteOffer>> = MutableLiveData()
    private var responseGetApplicationById : MutableLiveData<Event<ResponseGetApplicationByIdRecruiter>> = MutableLiveData()
    private var responseMarkAsSeen: MutableLiveData<Event<ResponseMarkAsSeen>> = MutableLiveData()
    private var responseMarkAsSelected: MutableLiveData<Event<ResponseMarkAsSelected>> = MutableLiveData()
    private var currentOfferId: MutableLiveData<String> = MutableLiveData()
    private var currentApplicationId: MutableLiveData<String> = MutableLiveData()
    private val currentDomainName: MutableLiveData<String> = MutableLiveData()
    private var errorString: MutableLiveData<Event<String>> = MutableLiveData()

    //Functions to expose all MutableLiveData instances as LiveData instances
    fun responseAddOffer() : LiveData<Event<ResponseAddOffer>> = responseAddOffer
    fun responseGetOffersByRecruiter() : LiveData<ResponseGetOffersByRecruiter> = responseGetOffersByRecruiter
    fun responseGetOfferByIdRecruiter(): LiveData<Event<ResponseGetOfferByIdRecruiter>> = responseGetOfferByIdRecruiter
    fun responseGetOfferApplicants() : LiveData<Event<ResponseGetOfferApplicants>> = responseGetOfferApplicants
    fun responseUpdateOffer(): LiveData<Event<ResponseUpdateOffer>> = responseUpdateOffer
    fun responseToggleVisibility(): LiveData<Event<ResponseToggleVisibility>> = responseToggleVisibility
    fun responseDeleteOffer(): LiveData<Event<ResponseDeleteOffer>> = responseDeleteOffer
    fun responseGetApplicationById() : LiveData<Event<ResponseGetApplicationByIdRecruiter>> = responseGetApplicationById
    fun responseMarkAsSeen() : LiveData<Event<ResponseMarkAsSeen>> = responseMarkAsSeen
    fun responseMarkAsSelected(): LiveData<Event<ResponseMarkAsSelected>> = responseMarkAsSelected
    fun errorString(): LiveData<Event<String>> = errorString

    fun getOffersByRecruiter(){
        val token:String = profileRepository.getToken()
        val recruiterID: String = profileRepository.getUserId()
        if(token == "") {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        if(recruiterID == ""){
            errorString.postValue(Event("Invalid User ID. Please log in again."))
            return
        }
        viewModelScope.launch {
            try {
                responseGetOffersByRecruiter.postValue(recruiterRepository.getOffersByRecruiter("Bearer $token", recruiterID))
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun getOfferByIdRecruiter(offerID:String){
        val token = profileRepository.getToken()
        if(token == "") {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                currentOfferId.postValue(offerID)
                responseGetOfferByIdRecruiter.postValue(
                    Event(recruiterRepository.getOfferByIdRecruiter("Bearer $token", offerID)))
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun getOfferApplicants(){
        val token = profileRepository.getToken()
        if(token == "") {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        val offerID = currentOfferId.value!!
        viewModelScope.launch {
            try {
                responseGetOfferApplicants.postValue(
                    Event(recruiterRepository.getOfferApplicants("Bearer $token", offerID)))
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun updateOffer(offerName: String, requirements: String, skills:String, expectation: String){
        val token = profileRepository.getToken()
        if(token == "") {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        val bodyUpdateOffer = BodyUpdateOffer(offerName, requirements, skills, expectation)
        val offerID = currentOfferId.value!!
        viewModelScope.launch {
            try {
                responseUpdateOffer.postValue(Event(recruiterRepository.updateOffer("Bearer $token", offerID, bodyUpdateOffer)))
                //refreshOffer()
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun toggleVisibility(isChecked: Boolean){
        val token = profileRepository.getToken()
        if(token == "") {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        val bodyToggleVisibility = BodyToggleVisibility(isChecked)
        val offerID = currentOfferId.value!!
        viewModelScope.launch {
            try {
                responseToggleVisibility.postValue(Event(recruiterRepository.toggleVisibility("Bearer $token", offerID, bodyToggleVisibility)))
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun deleteOffer(){
        val token = profileRepository.getToken()
        if(token == "") {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        val offerID = currentOfferId.value!!
        viewModelScope.launch {
            try {
                responseDeleteOffer.postValue(Event(recruiterRepository.deleteOffer("Bearer $token", offerID)))
                currentOfferId.postValue(OFFERS_REQUESTED_ONCE.toString())
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun getApplicationById(applicationID:String){
        val token = profileRepository.getToken()
        if(token == "") {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                currentApplicationId.postValue(applicationID)
                responseGetApplicationById.postValue(
                    Event(recruiterRepository.getApplicationById("Bearer $token", applicationID)))
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    fun updateSeenSelected(applicationID: String?, type: UpdateType, value: Boolean?){
        val token = profileRepository.getToken()
        if(token == "") {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }

        var appId = applicationID
        var status = value
        if(appId == null){
            appId = currentApplicationId.value!!
        }
        if(status == null) {
            status = true
        }

        viewModelScope.launch {
            try {
                if(type == UpdateType.SEEN){
                    val response = recruiterRepository.markSeen("Bearer $token", appId, BodyMarkAsSeen(status))
                    responseMarkAsSeen.postValue(Event(response))
                }else{
                    val response = recruiterRepository.markSelected("Bearer $token", appId, BodyMarkAsSelected(status))
                    responseMarkAsSelected.postValue(Event(response))
                }

                editApplicationList(appId, type)

                //updateSeenSelected is called from Application Details fragment
                if(applicationID == null)
                    refreshApplication(status, type)

            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    private fun refreshApplication(status: Boolean, type: UpdateType) {
        val response = responseGetApplicationById.value!!.peekContent()
        if(type == UpdateType.SEEN)
            response.application!!.is_Seen = status
        else
            response.application!!.is_Selected = status
        responseGetApplicationById.postValue(Event(response))
    }

    private fun editApplicationList(appId: String, type: UpdateType) {
        (responseGetOfferApplicants.value!!.peekContent().applicants as ArrayList<ApplicantCardModel>).let { list ->
            for (listItem in list) {
                if (listItem.application_id == appId) {
                    if(type == UpdateType.SEEN)
                        listItem.is_Seen = true
                    else
                        listItem.is_Selected = true
                    refreshApplicants(list)
                    break
                }
            }
        }
    }

    private fun refreshApplicants(applicantList: ArrayList<ApplicantCardModel>){
        val response = responseGetOfferApplicants.value!!.peekContent()
        response.applicants = applicantList
        responseGetOfferApplicants.postValue(Event(response))
    }

    fun addOffer(offerName:String, requirements: String, skills: String, expectation: String){
        val token = profileRepository.getToken()
        val recruiterID: String = profileRepository.getUserId()
        if(token == "") {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        if(recruiterID == ""){
            errorString.postValue(Event("Invalid User ID. Please log in again."))
            return
        }
        val domainName = currentDomainName.value!!
        Log.d("Hi", "addOffer: $domainName")
        viewModelScope.launch {
            try {
                val bodyAddOffer = BodyAddOffer(offerName, domainName, requirements, skills, expectation, recruiterID)
                responseAddOffer.postValue(Event(recruiterRepository.addOffer(bodyAddOffer, "Bearer $token")))
                getOffersByRecruiter()
                getOffersByDomain(currentDomainName.value!!)
            } catch(e: Exception){
                handleError(e, errorString)
            }
        }
    }

    private fun setDomain(domainName: String){
        currentDomainName.postValue(domainName)
    }

    private var responseUpdateApplication: MutableLiveData<Event<ResponseUpdateApplication>> = MutableLiveData()
    private var responseDeleteApplication: MutableLiveData<Event<ResponseDeleteApplication>> = MutableLiveData()
    private var responseGetApplicationsByCandidate: MutableLiveData<ResponseGetApplicationsByCandidate> = MutableLiveData()
    private var responseGetApplicationDetailByIdCandidate: MutableLiveData<Event<ResponseGetApplicationDetailByIdCandidate>> = MutableLiveData()
    private var responseGetOffersByDomain: MutableLiveData<ResponseGetOffersByDomain> = MutableLiveData()
    private var responseAddApplication: MutableLiveData<Event<ResponseAddApplication>> = MutableLiveData()
    private var responseGetOfferById: MutableLiveData<ResponseGetOfferById> = MutableLiveData()
    private var safeToVisitDomainOffers: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private var safeToVisitDomainOfferDetails: MutableLiveData<Event<Boolean>> = MutableLiveData()

    fun responseUpdateApplication(): LiveData<Event<ResponseUpdateApplication>> = responseUpdateApplication
    fun responseDeleteApplication(): LiveData<Event<ResponseDeleteApplication>> = responseDeleteApplication
    fun responseGetApplicationsByCandidate(): LiveData<ResponseGetApplicationsByCandidate> = responseGetApplicationsByCandidate
    fun responseGetApplicationDetailByIdCandidate(): LiveData<Event<ResponseGetApplicationDetailByIdCandidate>> = responseGetApplicationDetailByIdCandidate
    fun responseGetOffersByDomain(): LiveData<ResponseGetOffersByDomain> = responseGetOffersByDomain
    fun responseAddApplication(): LiveData<Event<ResponseAddApplication>> = responseAddApplication
    fun responseGetOfferById(): LiveData<ResponseGetOfferById> = responseGetOfferById
    fun safeToVisitDomainOffers(): LiveData<Event<Boolean>> = safeToVisitDomainOffers
    fun safeToVisitDomainOfferDetails(): LiveData<Event<Boolean>> = safeToVisitDomainOfferDetails

    fun getApplicationsByCandidate() {
        val token: String = profileRepository.getToken()
        val applicantID: String = profileRepository.getUserId()
        if (token == "") {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        if (applicantID == "") {
            errorString.postValue(Event("Invalid User ID. Please log in again."))
            return
        }
        viewModelScope.launch {
            try {
                responseGetApplicationsByCandidate.postValue(candidateRepository.getApplicationByCandidate("Bearer $token", applicantID))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun getApplicationDetailByIdCandidate(applicationID: String) {
        val token = profileRepository.getToken()
        if (token == "") {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                currentApplicationId.postValue(applicationID)
                responseGetApplicationDetailByIdCandidate.postValue(
                    Event(candidateRepository.getApplicationDetailByIdCandidate("Bearer $token", applicationID)))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }


    fun updateApplication(resume:String, previousWork:String) {
        val token = profileRepository.getToken()
        if (token == "") {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        val applicationID = currentApplicationId.value!!
        val bodyUpdateApplication = BodyUpdateApplication(resume, previousWork)
        viewModelScope.launch {
            try {
                responseUpdateApplication.postValue(Event(candidateRepository.updateApplication("Bearer $token", applicationID, bodyUpdateApplication)))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }


    fun deleteApplication() {
        val token = profileRepository.getToken()
        if (token == "") {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        val applicationID = currentApplicationId.value!!
        viewModelScope.launch {
            try {
                responseDeleteApplication.postValue(Event(candidateRepository.deleteApplication("Bearer $token", applicationID)))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun getOffersByDomain(domainName: String) {
        val token = profileRepository.getToken()
        if (token == "") {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                setDomain(domainName)
                responseGetOffersByDomain.postValue(candidateRepository.getOffersByDomain("Bearer $token", domainName))
                safeToVisitDomainOffers.postValue(Event(true))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun addApplication(Resume: String, PreviousWork: String) {
        val token = profileRepository.getToken()
        val applicantID: String = profileRepository.getUserId()
        if (token == "") {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        val offerId = currentOfferId.value!!
        viewModelScope.launch {
            try {
                val bodyAddApplication = BodyAddApplication(Date(), Resume, PreviousWork, applicantID, offerId)
                responseAddApplication.postValue(Event(candidateRepository.addApplication("Bearer $token", bodyAddApplication)))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun getOfferById(offerId: String) {
        val token = profileRepository.getToken()
        if (token == "") {
            errorString.postValue(Event(INVALID_TOKEN))
            return
        }
        viewModelScope.launch {
            try {
                currentOfferId.postValue(offerId)
                responseGetOfferById.postValue(candidateRepository.getOfferById("Bearer $token", offerId))
                safeToVisitDomainOfferDetails.postValue(Event(true))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    //Nullify Functions
    fun nullifySafeToVisitDomainOffers(){
        safeToVisitDomainOffers.postValue(Event(false))
    }
    fun nullifySafeToVisitDomainOfferDetails(){
        safeToVisitDomainOfferDetails.postValue(Event(false))
    }

    fun getLocalProfile() = profileRepository.getLocalProfile()

    companion object {
        const val INVALID_TOKEN = "Invalid Token. Please log in again."
        const val OFFERS_REQUESTED_ONCE = 0
    }

    enum class UpdateType{
        SEEN, SELECTED
    }
}