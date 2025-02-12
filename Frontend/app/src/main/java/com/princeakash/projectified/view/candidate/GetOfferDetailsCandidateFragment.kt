package com.princeakash.projectified.view.candidate

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.princeakash.projectified.R
import com.princeakash.projectified.model.candidate.ResponseGetOfferById
import com.princeakash.projectified.databinding.FragApplyOpportunityViewBinding
import com.princeakash.projectified.viewmodel.RecruiterCandidateViewModel

class GetOfferDetailsCandidateFragment : Fragment(R.layout.frag_apply_opportunity_view) {

    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel
    private lateinit var binding: FragApplyOpportunityViewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragApplyOpportunityViewBinding.bind(view)
        binding.buttonApply.setOnClickListener { findNavController().navigate(R.id.view_details_to_apply_opportunity) }
        binding.buttonCancel.setOnClickListener { findNavController().navigate(R.id.back_to_offers_by_domain) }

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(
            RecruiterCandidateViewModel::class.java)
        subscribeToObservers()

        if(savedInstanceState==null)
            recruiterCandidateViewModel.nullifySafeToVisitDomainOfferDetails()
    }

    private fun subscribeToObservers() {
        recruiterCandidateViewModel.responseGetOfferById().observe(viewLifecycleOwner, {
            populateViews(it)
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Offer Details"
    }

    private fun populateViews(it: ResponseGetOfferById) {
        binding.apply {
            if (it.code == 200) {
                textViewExpectation.setText(it.offer.expectation)
                textViewSkills.setText(it.offer.skills)
                textViewRequirement.setText(it.offer.requirements)
                textViewName.setText(it.offer.recruiter_name)
                textViewCollege.setText(it.offer.recruiter_collegeName)
                textViewSemester.setText(it.offer.recruiter_semester)
                textViewCourse.setText(it.offer.recruiter_course)
                progressCircularLayout.visibility = View.INVISIBLE
            }
        }
    }
}