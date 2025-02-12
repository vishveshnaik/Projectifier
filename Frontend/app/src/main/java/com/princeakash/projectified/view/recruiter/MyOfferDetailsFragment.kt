package com.princeakash.projectified.view.recruiter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.FragMyOfferDetailsBinding
import com.princeakash.projectified.model.recruiter.ResponseGetOfferByIdRecruiter
import com.princeakash.projectified.utils.Event
import com.princeakash.projectified.viewmodel.RecruiterCandidateViewModel

class MyOfferDetailsFragment: Fragment(R.layout.frag_my_offer_details) {

    private lateinit var listener: CompoundButton.OnCheckedChangeListener

    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel
    private lateinit var binding: FragMyOfferDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragMyOfferDetailsBinding.bind(view)
        binding.buttonDelist.setOnClickListener { deListOpportunity() }
        binding.buttonEditDetails.setOnClickListener { updateOffer() }
        binding.buttonViewApplicants.setOnClickListener { viewApplicants() }
        listener = CompoundButton.OnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            run {
                binding.progressCircularLayout.visibility = View.VISIBLE
                recruiterCandidateViewModel.toggleVisibility(isChecked)
            }
        }

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(
            RecruiterCandidateViewModel::class.java)
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        recruiterCandidateViewModel.responseGetOfferByIdRecruiter().observe(viewLifecycleOwner, {
            populateViews(it)
            binding.progressCircularLayout.visibility = View.INVISIBLE
        })

        recruiterCandidateViewModel.responseToggleVisibility().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.responseUpdateOffer().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.responseDeleteOffer().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_LONG).show()
                findNavController().navigate(R.id.offer_details_to_my_offers)
            }
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.responseGetOfferApplicants().observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let { response ->
                if(response.code == 200){
                    findNavController().navigate(R.id.offer_details_to_view_applicants)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Offer Details"
    }

    private fun viewApplicants() {
        binding.progressCircularLayout.visibility = View.VISIBLE
        recruiterCandidateViewModel.getOfferApplicants()
    }

    private fun updateOffer() {
        binding.apply{
            if (editTextOfferName.text.toString().isEmpty()) {
                editTextOfferName.error = "Enter offer name."
                return
            }

            if (editTextRequirements.text.toString().isEmpty()) {
                editTextRequirements.error = "Enter requirements."
                return
            }

            if (editTextSkills.text.toString().isEmpty()) {
                editTextSkills.error = "Enter skills."
                return
            }

            if (editTextExpectations.text.toString().isEmpty()) {
                editTextExpectations.error = "Enter expectation."
                return
            }

            val offerName = editTextOfferName.text.toString()
            val requirement = editTextRequirements.text.toString()
            val skills = editTextSkills.text.toString()
            val expectation = editTextExpectations.text.toString()

            progressCircularLayout.visibility = View.VISIBLE

            recruiterCandidateViewModel.updateOffer(offerName, requirement, skills, expectation)
        }
    }

    private fun populateViews(event: Event<ResponseGetOfferByIdRecruiter>) {
        binding.apply{
            event.getContentIfNotHandled()?.let { response ->
                switchAllow.setOnCheckedChangeListener(null)
                editTextOfferName.setText(response.offer?.offer_name)
                editTextRequirements.setText(response.offer?.requirements)
                editTextSkills.setText(response.offer?.skills)
                editTextExpectations.setText(response.offer?.expectation)
                switchAllow.isChecked = (response.offer?.is_visible != null) && (response.offer?.is_visible == true)
                switchAllow.setOnCheckedChangeListener(listener)
            }
        }
    }

    private fun deListOpportunity() {
        AlertDialog.Builder(requireContext())
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this offer? You cannot undo this action later.")
                .setPositiveButton("Yes") { _, _ ->
                    binding.progressCircularLayout.visibility = View.VISIBLE
                    recruiterCandidateViewModel.deleteOffer()
                }
                .setNegativeButton("No") { _, _ -> }
                .create().show()
    }
}