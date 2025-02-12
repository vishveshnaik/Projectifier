package com.princeakash.projectified.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.princeakash.projectified.utils.Event
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.MyApplication.Companion.handleError
import com.princeakash.projectified.model.user.*
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    //ProfileRepository for all types of Profile-Related Operations
    private val profileRepository = (app as MyApplication).profileRepository

    //MutableLiveData for the data to be exposed
    private var errorString: MutableLiveData<Event<String>> = MutableLiveData()
    private var responseSignUp: MutableLiveData<Event<ResponseSignUp>> = MutableLiveData()
    private var responseLogin: MutableLiveData<Event<ResponseLogin>> = MutableLiveData()
    private var responseUpdateProfile: MutableLiveData<Event<ResponseUpdateProfile>> = MutableLiveData()
    private var responseCheckSignUp: MutableLiveData<Event<ResponseSignUp>> = MutableLiveData()
    private var responseGenerateOtp: MutableLiveData<Event<ResponseGenerateOtp>> = MutableLiveData()
    private var responseVerifyOtp: MutableLiveData<Event<ResponseVerifyOtp>> = MutableLiveData()
    private var responseUpdatePassword: MutableLiveData<Event<ResponseUpdatePassword>> = MutableLiveData()
    private var bodySignUp: MutableLiveData<BodySignUp> = MutableLiveData()

    //LiveData for all exposed data
    fun errorString(): LiveData<Event<String>> = errorString
    fun responseSignUp(): LiveData<Event<ResponseSignUp>> = responseSignUp
    fun responseLogin(): LiveData<Event<ResponseLogin>> = responseLogin
    fun responseUpdateProfile(): LiveData<Event<ResponseUpdateProfile>> = responseUpdateProfile
    fun responseCheckSignUp(): LiveData<Event<ResponseSignUp>> = responseCheckSignUp
    fun responseGenerateOtp(): LiveData<Event<ResponseGenerateOtp>> = responseGenerateOtp
    fun responseVerifyOtp(): LiveData<Event<ResponseVerifyOtp>> = responseVerifyOtp
    fun responseUpdatePassword(): LiveData<Event<ResponseUpdatePassword>> = responseUpdatePassword
    fun bodySignUp(): LiveData<BodySignUp> = bodySignUp

    //Functions based on *Local Data*
    fun setUserId(id: String) = profileRepository.setUserId(id)

    var loginStatus: Boolean
        get() = profileRepository.getLoginStatus()
        set(status) = profileRepository.setLoginStatus(status)

    var profileStatus: Boolean
        get() = profileRepository.getProfileStatus()
        set(status) = profileRepository.setProfileStatus(status)

    var localProfile: ProfileModel?
        get() = profileRepository.getLocalProfile()
        set(bodyModel) = profileRepository.setLocalProfile(bodyModel!!)

    var token: String
        get() = profileRepository.getToken()
        set(newToken) = profileRepository.setToken(newToken)

    var resetEmail: String
        get() = profileRepository.getResetEmail()
        set(email) = profileRepository.setResetEmail(email)

    var userName: String
        get() = profileRepository.getUserName()
        set(name) = profileRepository.setUserName(name)

    var darkModeStatus: Boolean
        get() = profileRepository.getDarkModeStatus()
        set(status) = profileRepository.setDarkModeStatus(status)

    //Functions based on *Server Data*
    fun signUp() {
        viewModelScope.launch {
            try {
                val response = profileRepository.signUp(bodySignUp.value!!)
                if (response.code == 200)
                    responseSignUp.postValue(Event(response))
                else
                    errorString.postValue(Event(response.message))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun checkSignUp(name:String, email:String, phone:String, password: String) {
        viewModelScope.launch {
            try {
                val body = BodySignUp(name, email, phone, password)
                val response = profileRepository.checkSignUp(body)
                when(response.code){
                    200 -> {
                        responseCheckSignUp.postValue((Event(response)))
                        bodySignUp.postValue(body)
                    }
                    300 -> responseCheckSignUp.postValue((Event(response)))
                    else -> errorString.postValue(Event(response.message))
                }
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun logIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                val bodyLogin = BodyLogin(email, password)
                val response = profileRepository.logIn(bodyLogin)
                if (response.code == 200) {
                    setUserId(response._id!!)
                    loginStatus = true
                    token = response.token!!
                    profileStatus = response.profileCompleted!!
                    userName = response.name!!
                    if (profileStatus) {
                        val bodyProfile = ProfileModel(response.name!!,
                                response.collegeName!!, response.course!!,
                                response.semester!!, response.languages!!,
                                response.interest1!!, response.interest2!!,
                                response.interest3!!, response.description!!,
                                response.hobbies!!)
                        localProfile = bodyProfile
                    }
                }
                responseLogin.postValue(Event(response))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    //Special logIn function for SignUp-Login flow
    fun logIn() = logIn(bodySignUp.value!!.email, bodySignUp.value!!.password)

    fun updateProfile(userName: String, college: String, course: String, semester: String,
                      num: IntArray, interest1: String, interest2: String, interest3: String,
                      description: String, hobbies: String){
        viewModelScope.launch {
            try {
                val bodyUpdateProfile = BodyUpdateProfile(userName, college, course, semester, num,
                    interest1, interest2, interest3, description, hobbies, profileRepository.getUserId())
                val response = profileRepository.updateProfile("Bearer $token", bodyUpdateProfile)
                if(response.code == 200){
                    profileStatus = true
                    localProfile = ProfileModel(bodyUpdateProfile)
                }
                responseUpdateProfile.postValue(Event(response))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun generateOtp(email: String){
        viewModelScope.launch {
            try {
                resetEmail = email
                val bodyGenerateOtp = BodyGenerateOtp(email)
                responseGenerateOtp.postValue(Event(profileRepository.generateOtp(bodyGenerateOtp)))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun verifyOtp(otp: String){
        viewModelScope.launch {
            try {
                val bodyVerifyOtp = BodyVerifyOtp(resetEmail, otp)
                responseVerifyOtp.postValue(Event(profileRepository.verifyOtp(bodyVerifyOtp)))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun updatePassword(newPassword: String){
        viewModelScope.launch {
            try {
                val bodyUpdatePassword = BodyUpdatePassword(resetEmail, newPassword)
                responseUpdatePassword.postValue(Event(profileRepository.updatePassword(bodyUpdatePassword)))
            } catch (e: Exception) {
                handleError(e, errorString)
            }
        }
    }

    fun isJWTExpired() : Boolean
        = JWT(token).isExpired((10).toLong())
}