package com.princeakash.projectified

import android.app.Application
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.princeakash.projectified.repository.FaqRepository
import com.princeakash.projectified.repository.CandidateRepository
import com.princeakash.projectified.repository.RecruiterRepository
import com.princeakash.projectified.repository.ProfileRepository
import com.princeakash.projectified.utils.CustomDateAdapter
import com.princeakash.projectified.utils.Event
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.Exception


class MyApplication : Application() {

    //Moshi: GSON of Kotlin
    lateinit var moshi: Moshi
    lateinit var retrofit: Retrofit

    lateinit var recruiterRepository: RecruiterRepository
    lateinit var profileRepository: ProfileRepository
    lateinit var candidateRepository: CandidateRepository
    lateinit var faqRepository: FaqRepository

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        moshi = Moshi.Builder()
                .add(CustomDateAdapter())
                .add(KotlinJsonAdapterFactory())
                .build()
        retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(BASE_URL)
                .build()

        recruiterRepository = RecruiterRepository(retrofit)
        profileRepository = ProfileRepository(retrofit, this)
        candidateRepository = CandidateRepository(retrofit)
        faqRepository = FaqRepository(retrofit)
    }

    //Show Toast by using Application Context
    fun showToast(message: String?) {
        Toast.makeText(this, message, LENGTH_SHORT).show()
    }

    //Storing static values
    companion object {
        val BASE_URL = "https://am-ka-projectified-test.herokuapp.com/"
        //val BASE_URL = "http://192.168.1.4:3000/"
        fun handleError(e: Exception, errorString: MutableLiveData<Event<String>>){
            e.printStackTrace()

            //Change the Mutable LiveData so that change can be detected in Fragment/Activity. One extra Observer per ViewModel per Activity
            errorString.postValue(Event(e.localizedMessage))
        }
    }
}