package com.princeakash.projectified.repository

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.model.user.*
import com.princeakash.projectified.service.ProfileService
import com.squareup.moshi.JsonAdapter
import retrofit2.Retrofit
import kotlin.Exception

//TODO:Show exception via toast.
class ProfileRepository(retrofit: Retrofit, app: MyApplication) {

    var profileService: ProfileService = retrofit.create(ProfileService::class.java)
    var application: MyApplication = app
    var sharedPref: SharedPreferences
    var editor: SharedPreferences.Editor

    init{
        sharedPref = application.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    suspend fun signUp(bodySignUp: BodySignUp) = profileService.signUp(bodySignUp)

    suspend fun logIn(bodyLogin: BodyLogin) = profileService.logIn(bodyLogin)

    suspend fun checkSignUp(bodySignUp: BodySignUp)= profileService.checkSignup(bodySignUp)

    //suspend fun createProfile(token: String,bodyCreateProfile: BodyCreateProfile)  = profileService.createProfile(token, bodyCreateProfile)

    suspend fun updateProfile(token: String,bodyUpdateProfile: BodyUpdateProfile): ResponseUpdateProfile {
        val profileID = getUserId()
        return profileService.updateProfile(token,bodyUpdateProfile, profileID)
    }

    suspend fun generateOtp(bodyGenerateOtp: BodyGenerateOtp) = profileService.generateOtp(bodyGenerateOtp)

    suspend fun verifyOtp(bodyVerifyOtp: BodyVerifyOtp) = profileService.verifyOtp(bodyVerifyOtp)

    suspend fun updatePassword(bodyUpdatePassword: BodyUpdatePassword) = profileService.updatePassword(bodyUpdatePassword)

    fun getLoginStatus(): Boolean{
        try{
            val loginStatus = sharedPref.getBoolean(LOGIN_STATUS, false)
            return loginStatus
        } catch (e: Exception){
            e.printStackTrace()
            return false
        }
    }

    fun setLoginStatus(loginStatus: Boolean) {
        try{
            editor.putBoolean(LOGIN_STATUS, loginStatus)
            editor.commit()
        } catch(e : Exception){
            e.printStackTrace()
        }
    }

    fun getToken() : String {
        try{
            return sharedPref.getString(USER_TOKEN, "")!!
        } catch (e: Exception){
            e.printStackTrace()
            return ""
        }
    }

    fun setToken(token : String){
        try{
            if(token.equals(""))
                throw NullPointerException()
            editor.putString(USER_TOKEN, token)
            editor.commit()
        } catch(e: Exception){
            e.printStackTrace()
            //TODO:Show "Error in token" Toast
        }
    }

    fun getLocalProfile() : ProfileModel?{
        try{
            val json = sharedPref.getString(USER_PROFILE, null)!!
            val jsonAdapter:JsonAdapter<ProfileModel> = (application).moshi.adapter(ProfileModel::class.java)
            return jsonAdapter.fromJson(json)!!
        } catch(e: Exception){
            e.printStackTrace()
            return null
            //TODO:Show "Error in token" Toast
        }
    }

    fun setLocalProfile(bodyProfile: ProfileModel){
        try{
            val jsonAdapter = (application).moshi.adapter(ProfileModel::class.java)
            val json = jsonAdapter.toJson(bodyProfile)
            editor.putString(USER_PROFILE, json)
            editor.commit()
        } catch (e: Exception){
            e.printStackTrace()
            //TODO:Show "Error in storing profile locally" Toast
        }
    }

    fun getUserId(): String{
        try{
            return sharedPref.getString(USER_ID, null)!!
        } catch(e: Exception){
            e.printStackTrace()
            return ""
            //TODO:Show "Error in token" Toast
        }
    }

    fun setUserId(id: String){
        try{
            if(id.equals(""))
                throw NullPointerException()
            editor.putString(USER_ID, id)
            editor.commit()
        } catch(e: Exception){
            e.printStackTrace()
            //TODO:Show "Error in token" Toast
        }
    }

    fun getProfileStatus(): Boolean{
        try{
            return sharedPref.getBoolean(PROFILE_STATUS, false)
        }catch (e: Exception){
            e.printStackTrace()
            return false
        }
    }

    fun setProfileStatus(profileStatus: Boolean){
        try{
            editor.putBoolean(PROFILE_STATUS, profileStatus)
            editor.commit()
        } catch(e : Exception){
            e.printStackTrace()
        }
    }

    fun getResetEmail() : String {
        try{
            return sharedPref.getString(RESET_EMAIL, "")!!
        } catch (e: Exception){
            e.printStackTrace()
            return ""
        }
    }

    fun setResetEmail(email : String){
        try{
            if(email.equals(""))
                throw NullPointerException()
            editor.putString(RESET_EMAIL, email)
            editor.commit()
        } catch(e: Exception){
            e.printStackTrace()
        }
    }

    fun getUserName() : String {
        try{
            return sharedPref.getString(USER_NAME, "")!!
        } catch (e: Exception){
            e.printStackTrace()
            return ""
        }
    }

    fun setUserName(name : String){
        try{
            if(name.equals(""))
                throw NullPointerException()
            editor.putString(USER_NAME, name)
            editor.commit()
        } catch(e: Exception){
            e.printStackTrace()
        }
    }

    fun getDarkModeStatus() = sharedPref.getBoolean("NightMode", false)

    fun setDarkModeStatus(status: Boolean){
        try{
            editor.putBoolean("NightMode",status)
            editor.commit()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    companion object{
        val SHARED_PREFS = "SharedPreferences"
        val LOGIN_STATUS = "LoginStatus"
        val USER_ID = "UserId"
        val USER_PROFILE = "UserProfile"
        val USER_TOKEN = "UserToken"
        val USER_NAME = "UserName"
        val PROFILE_STATUS = "ProfileStatus"
        val RESET_EMAIL = "ResetUserEmail"
    }
}


