package com.princeakash.projectified.view.candidate

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.princeakash.projectified.R
import com.princeakash.projectified.model.candidate.ResponseGetApplicationDetailByIdCandidate
import com.princeakash.projectified.databinding.FragMyApplicationDetailsBinding
import com.princeakash.projectified.utils.Event
import com.princeakash.projectified.viewmodel.RecruiterCandidateViewModel
import java.net.URL

class MyApplicationDetailsFragment : Fragment(R.layout.frag_my_application_details) {

    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel
    private lateinit var binding: FragMyApplicationDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragMyApplicationDetailsBinding.bind(view)
        binding.buttonUpdateDetails.setOnClickListener { updateOffer() }
        binding.buttonDeleteApplication.setOnClickListener { deleteApplication() }

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(
            RecruiterCandidateViewModel::class.java)
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        recruiterCandidateViewModel.responseGetApplicationDetailByIdCandidate().observe(viewLifecycleOwner, {
            binding.progressCircularLayout.visibility = View.INVISIBLE
            populateViews(it)
        })


        recruiterCandidateViewModel.responseUpdateApplication().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
            }
        })


        recruiterCandidateViewModel.responseDeleteApplication().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.details_to_my_applications_home)
            }
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Application Details"
    }

    private fun updateOffer() {
        binding.apply {
            if (editTextPreviousWork.text.toString().isEmpty()){
                editTextPreviousWork.error = "Please Provide Drive Link of Your Previous Work."
                return
            }
            if (editTextResume.text.toString().isEmpty()){
                editTextResume.error = "Please Provide Drive Link of the Resume."
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

            progressCircularLayout.visibility = View.VISIBLE
            recruiterCandidateViewModel.updateApplication(resume, previousWork)
        }
    }

    private fun populateViews(event: Event<ResponseGetApplicationDetailByIdCandidate>) {
        event.getContentIfNotHandled()?.let { response ->
            binding.apply {
                if (response.code == 200) {
                    textViewRequirement.text = response.application?.requirements
                    textViewSkills.text = response.application?.skills
                    textViewExpectation.text = response.application?.expectation
                    textViewName.text = response.application?.recruiter_name
                    textViewCollege.text = response.application?.recruiter_collegeName
                    textViewSemester.text = response.application?.recruiter_semester
                    textViewCourse.text = response.application?.recruiter_course
                    editTextPreviousWork.setText(response.application?.previousWork)
                    editTextResume.setText(response.application?.resume)

                    if (response.application?.is_Seen!!)
                        imageViewSeen.setImageResource(R.drawable.ic_baseline_favorite_24)
                    else
                        imageViewSeen.setImageResource((R.drawable.ic_outline_favorite_border_24))

                    if (response.application?.is_Selected!!)
                        imageViewSelected.setImageResource(R.drawable.ic_baseline_done_24)
                    else
                        imageViewSelected.setImageResource((R.drawable.ic_baseline_done_outline_24))
                    progressCircularLayout.visibility = View.INVISIBLE
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
                progressCircularLayout.visibility = View.INVISIBLE
            }
        }
    }

    private fun deleteApplication() {
        AlertDialog.Builder(requireContext())
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this application? You cannot undo this action later.")
                .setPositiveButton("Yes") { _,_ ->
                    binding.progressCircularLayout.visibility = View.VISIBLE
                    recruiterCandidateViewModel.deleteApplication()
                }
                .setNegativeButton("No") { _,_ -> }
                .create().show()
    }
}