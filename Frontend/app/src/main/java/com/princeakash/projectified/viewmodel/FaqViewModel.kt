package com.princeakash.projectified.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.princeakash.projectified.utils.Event
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.model.faq.BodyAddQuestion
import com.princeakash.projectified.model.faq.ResponseAddQuestion
import com.princeakash.projectified.model.faq.ResponseGetFaq
import kotlinx.coroutines.launch
import kotlin.Exception

class FaqViewModel(val app: Application): AndroidViewModel(app) {

    //FaqRepository
    private val faqRepository = (app as MyApplication).faqRepository

    //MutableLiveData
    private var responseGetFaq: MutableLiveData<ResponseGetFaq> = MutableLiveData()
    private var responseAddQuestion: MutableLiveData<Event<ResponseAddQuestion>> = MutableLiveData()
    private var errorString: MutableLiveData<Event<String>> = MutableLiveData()

    //LiveData
    fun responseGetFaq(): LiveData<ResponseGetFaq> = responseGetFaq
    fun responseAddQuestion():LiveData<Event<ResponseAddQuestion>> = responseAddQuestion
    fun errorString(): LiveData<Event<String>> = errorString

    fun getAllFaq(){
        viewModelScope.launch {
            try{
                responseGetFaq.postValue(faqRepository.getAllFaq())
            } catch(e: Exception){
                errorString.postValue(Event(e.localizedMessage))
            }
        }
    }

    fun addQuestion(question: String){
        viewModelScope.launch {
            try{
                val bodyAddQuestion = BodyAddQuestion(question)
                responseAddQuestion.postValue(Event(faqRepository.addQuestion(bodyAddQuestion)))
            }catch (e: Exception){
                errorString.postValue(Event(e.localizedMessage))
            }
        }
    }
}