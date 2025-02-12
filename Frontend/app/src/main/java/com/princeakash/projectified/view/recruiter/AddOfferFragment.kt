package com.princeakash.projectified.view.recruiter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.FragFloatOpportunityBinding
import com.princeakash.projectified.viewmodel.RecruiterCandidateViewModel

class AddOfferFragment : Fragment(R.layout.frag_float_opportunity) {

    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel
    private lateinit var binding: FragFloatOpportunityBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragFloatOpportunityBinding.bind(view)
        binding.EnlistButton.setOnClickListener { validateParameters() }
        binding.buttonCancel.setOnClickListener { findNavController().navigate(R.id.back_to_offers_by_domain) }

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(
            RecruiterCandidateViewModel::class.java)
        subscribeToObservers()

        loadRecruiterDetails()
    }

    private fun subscribeToObservers() {
        recruiterCandidateViewModel.responseAddOffer().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{ response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.safeToVisitDomainOffers().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let{safeToVisit->
                if(safeToVisit){
                    findNavController().navigate(R.id.back_to_offers_by_domain)
                }
            }
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{ message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, message, LENGTH_LONG).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Add Offer"
    }

    private fun validateParameters() {
        binding.apply{
            //Ensuring non-empty input
            if(editTextOfferName.text.isNullOrEmpty()){
                editTextOfferName.error = "Enter offer name."
                return
            }
            if(editTextRequirement.text.isNullOrEmpty()){
                editTextRequirement.error = "Enter requirements."
                return
            }
            if(editTextSkills.text.isNullOrEmpty()){
                editTextSkills.error = "Enter skills."
                return
            }
            if(editTextExpectation.text.isNullOrEmpty()){
                editTextExpectation.error = "Enter expectation."
                return
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Confirm")
                .setMessage("Are you sure you want to float this offer?")
                .setPositiveButton("Yes") { _,_ ->
                    binding.progressCircularLayout.visibility = View.VISIBLE
                    recruiterCandidateViewModel.addOffer(
                        offerName = editTextOfferName.text.toString(), requirements = editTextRequirement.text.toString(),
                        skills = editTextSkills.text.toString(), expectation = editTextExpectation.text.toString())
                }
                .setNegativeButton("No") { _,_ -> }
                .create().show()
        }
    }

    private fun loadRecruiterDetails() {
        binding.apply {
            progressCircularLayout.visibility = View.VISIBLE
            recruiterCandidateViewModel.getLocalProfile()?.let {
                textViewNameData.text = it.name
                textViewCollegeData.text = it.collegeName
                textViewCourseData.text = it.course
                textViewSemesterData.setText(it.semester)
            }
            progressCircularLayout.visibility = View.INVISIBLE
        }
    }
}