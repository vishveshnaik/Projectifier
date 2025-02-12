package com.princeakash.projectified.view.user

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.princeakash.projectified.view.MainActivity
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.CreateUpdateProfileBinding
import com.princeakash.projectified.viewmodel.ProfileViewModel
import com.princeakash.projectified.utils.HelperClass

class CreateProfileActivity : AppCompatActivity() {

    lateinit var profileViewModel: ProfileViewModel
    lateinit var binding: CreateUpdateProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.responseUpdateProfile().observe(this, {
            it?.getContentIfNotHandled()?.let { response ->
                Toast.makeText(this, response.message, LENGTH_LONG).show()
                if(response.code == 200){
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        })

        setAdapters()
        binding.editTextName.setText(profileViewModel.userName)
        binding.Save.setOnClickListener { validateParameters() }

        if(savedInstanceState == null)
            loadChips(IntArray(0))
        else
            savedInstanceState.getIntArray(HelperClass.CHIPS)?.let { arr -> loadChips(arr) }
    }

    private fun setAdapters() {
        val adapterCourse = ArrayAdapter.createFromResource(this, R.array.courses, android.R.layout.simple_spinner_dropdown_item)
        adapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editTextCourse.setAdapter(adapterCourse)

        val adapterSemester = ArrayAdapter.createFromResource(this, R.array.semesters, android.R.layout.simple_spinner_dropdown_item)
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editTextSemester.setAdapter(adapterSemester)

        val adapterInterest = ArrayAdapter.createFromResource(this, R.array.interests, android.R.layout.simple_spinner_dropdown_item)
        adapterInterest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editTextInterest1.setAdapter(adapterInterest)
        binding.editTextInterest2.setAdapter(adapterInterest)
        binding.editTextInterest3.setAdapter(adapterInterest)
    }

    private fun validateParameters() {
        binding.apply{
            if (editTextName.text.toString().isEmpty()) {
                editTextName.error = "Provide your college Name."
                return
            }

            if (editTextCollege.text.toString().isEmpty()) {
                editTextCollege.error = "Provide your college Name."
                return
            }

            if (editTextCourse.text.toString().isEmpty()) {
                editTextCourse.error = "Enter Course."
                return
            }
            if (editTextSemester.text.toString().isEmpty()) {
                editTextSemester.error = "Enter Semester."
                return
            }
            if (editTextInterest1.text.toString().isEmpty()) {
                editTextCourse.error = "Enter Course."
                return
            }
            if (editTextDescription.text.toString().isEmpty()) {
                editTextDescription.error = "Enter Description."
                return
            }
            if (editTextHobbies.text.toString().isEmpty()) {
                editTextHobbies.error = "Enter hobbies."
                return
            }

            profileViewModel.updateProfile(editTextName.text!!.toString(), editTextCollege.text!!.toString(),
                editTextCourse.text!!.toString(), editTextSemester.text!!.toString(), getChipStatusArray(),
                editTextInterest1.text!!.toString(), editTextInterest2.text!!.toString(),
                editTextInterest3.text!!.toString(), editTextDescription.text!!.toString(),
                editTextHobbies.text!!.toString())
        }
    }

    private fun loadChips(num: IntArray){
        val chips = HelperClass.getChipsArray(this)
        for(i in chips.indices){
            binding.chipGroupLanguages.addView(chips[i])
            chips[i].isChecked = (i < num.size && num[i]==1)
        }
    }

    private fun getChipStatusArray(): IntArray {
        var arr = IntArray(0)
        binding.chipGroupLanguages.children.forEach { v ->
            if (v is Chip) {
                arr = arr.plus(if (v.isChecked) 1 else 0)
            }
        }
        return arr
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putIntArray(HelperClass.CHIPS, getChipStatusArray())
        super.onSaveInstanceState(outState)
    }
}