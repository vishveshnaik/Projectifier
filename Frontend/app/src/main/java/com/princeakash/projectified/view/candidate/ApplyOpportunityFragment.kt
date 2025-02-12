package com.princeakash.projectified.view.candidate

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
import com.princeakash.projectified.databinding.FragApplyOpportunitySelfBinding
import com.princeakash.projectified.viewmodel.RecruiterCandidateViewModel
import java.net.URL

class ApplyOpportunityFragment : Fragment(R.layout.frag_apply_opportunity_self) {

    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel
    private lateinit var binding: FragApplyOpportunitySelfBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragApplyOpportunitySelfBinding.bind(view)
        binding.buttonSubmit.setOnClickListener { validateParameters() }
        binding.buttonCancel.setOnClickListener { findNavController().navigate(R.id.back_to_offers_by_domain) }

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(
            RecruiterCandidateViewModel::class.java)
        subscribeToObservers()

        loadLocalProfile()
    }

    private fun subscribeToObservers() {
        recruiterCandidateViewModel.responseAddApplication().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_LONG).show()
                if(response.code == "200"){
                    findNavController().navigate(R.id.back_to_offers_by_domain)
                }
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
            it?.getContentIfNotHandled()?.let { message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, message, LENGTH_LONG).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Apply Opportunity"
    }

    private fun validateParameters() {
        binding.apply {
            if (editTextPreviousWork.text.isNullOrEmpty()) {
                editTextPreviousWork.error = "Enter previous Work."
                return
            }
            if (editTextResume.text.isNullOrEmpty()) {
                editTextResume.error = "Enter resume link."
                return
            }

            val previousWork = editTextPreviousWork.text.toString()
            val resume = editTextResume.text.toString()

            try{
                URL(resume).toURI()
            }catch (e: Exception){
                editTextResume.error = "Enter a valid URL."
                return
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Confirm Application")
                .setMessage("Are you sure you want to apply for this offer with the filled details?")
                .setPositiveButton("Yes") { _,_ ->
                    progressCircularLayout.visibility = View.VISIBLE
                    recruiterCandidateViewModel.addApplication(resume, previousWork)
                }
                .setNegativeButton("No") { _,_ -> }
                .create().show()
        }
    }

    private fun loadLocalProfile() {
        binding.apply{
            progressCircularLayout.visibility = View.VISIBLE
            val profileModel = recruiterCandidateViewModel.getLocalProfile()!!
            textViewName.text = profileModel.name
            textViewCollege.text = profileModel.collegeName
            textViewCourse.text = profileModel.course
            textViewSemester.text = profileModel.semester
            progressCircularLayout.visibility = View.INVISIBLE
        }
    }
}