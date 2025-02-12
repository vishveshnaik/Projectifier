package com.princeakash.projectified.view.user

import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.CreateUpdateProfileBinding
import com.princeakash.projectified.viewmodel.ProfileViewModel
import com.princeakash.projectified.utils.HelperClass

class UpdateProfileFragment : Fragment(R.layout.create_update_profile) {

    //  ViewModels
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: CreateUpdateProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = CreateUpdateProfileBinding.bind(view)
        setAdapters()
        binding.Save.setOnClickListener { validateParameters() }

        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        subscribeToObservers()

        if (savedInstanceState == null)
            loadLocalProfile()
        else
            savedInstanceState.getIntArray(HelperClass.CHIPS)?.let { arr -> loadChips(arr) }
    }

    private fun loadChips(num: IntArray){
        val chips = HelperClass.getChipsArray(requireContext())
        for(i in chips.indices){
            binding.chipGroupLanguages.addView(chips[i])
            chips[i].isChecked = (i < num.size && num[i]==1)
        }
    }

    private fun setAdapters() {
        val adapterCourse = ArrayAdapter.createFromResource(requireContext(), R.array.courses, android.R.layout.simple_spinner_dropdown_item)
        adapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editTextCourse.setAdapter(adapterCourse)

        val adapterSemester = ArrayAdapter.createFromResource(requireContext(), R.array.semesters, android.R.layout.simple_spinner_dropdown_item)
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editTextSemester.setAdapter(adapterSemester)

        val adapterInterest = ArrayAdapter.createFromResource(requireContext(), R.array.interests, android.R.layout.simple_spinner_dropdown_item)
        adapterInterest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editTextInterest1.setAdapter(adapterInterest)
        binding.editTextInterest2.setAdapter(adapterInterest)
        binding.editTextInterest3.setAdapter(adapterInterest)
    }

    private fun subscribeToObservers() {
        profileViewModel.responseUpdateProfile().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_SHORT).show()
            }
        })
        profileViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, message, LENGTH_SHORT).show()
            }
        })
    }

    private fun loadLocalProfile() {
        val profileModel = profileViewModel.localProfile!!
        binding.apply{
            editTextName.setText(profileModel.name)
            editTextCollege.setText(profileModel.collegeName)
            editTextCourse.setText(profileModel.course)
            editTextSemester.setText(profileModel.semester)
            editTextInterest1.setText(profileModel.interest1)
            editTextInterest2.setText(profileModel.interest2)
            editTextInterest3.setText(profileModel.interest3)
            editTextDescription.setText(profileModel.description)
            editTextHobbies.setText(profileModel.hobbies)
            loadChips(num = profileModel.languages)
        }
    }

    private fun validateParameters() {
        binding.apply{
            if (editTextCollege.text.toString().isEmpty()){
                editTextCollege.error = "Provide your college Name."
                return
            }

            if (editTextCourse.text.toString().isEmpty()){
                editTextCourse.error = "Enter Course."
                return
            }
            if (editTextSemester.text.toString().isEmpty()){
                editTextSemester.error = "Enter Semester."
                return
            }
            if (editTextInterest1.text.toString().isEmpty()){
                editTextCourse.error = "Enter Course."
                return
            }
            if (editTextDescription.text.toString().isEmpty()){
                editTextDescription.error = "Enter Description."
                return
            }
            if (editTextHobbies.text.toString().isEmpty()) {
                editTextHobbies.error = "Enter hobbies."
                return
            }

            progressCircularLayout.visibility = View.VISIBLE
            profileViewModel.updateProfile(editTextName.text!!.toString(), editTextCollege.text!!.toString(),
                editTextCourse.text!!.toString(), editTextSemester.text!!.toString(), getChipStatusArray(),
                editTextInterest1.text!!.toString(), editTextInterest2.text!!.toString(),
                editTextInterest3.text!!.toString(), editTextDescription.text!!.toString(),
                editTextHobbies.text!!.toString())
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

    companion object {
        private const val TAG = "UpdateProfileFragment"
    }
}