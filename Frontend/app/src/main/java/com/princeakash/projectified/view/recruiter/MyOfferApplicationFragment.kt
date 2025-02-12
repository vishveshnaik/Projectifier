package com.princeakash.projectified.view.recruiter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.FragCandidateDetailsRecruiterBinding
import com.princeakash.projectified.model.recruiter.ResponseGetApplicationByIdRecruiter
import com.princeakash.projectified.utils.Event
import com.princeakash.projectified.viewmodel.RecruiterCandidateViewModel

class MyOfferApplicationFragment : Fragment(R.layout.frag_candidate_details_recruiter) {

    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel

    private lateinit var binding: FragCandidateDetailsRecruiterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragCandidateDetailsRecruiterBinding.bind(view)
        binding.textViewResume.setOnClickListener {
            try {
                val uri = Uri.parse(binding.textViewResume.text.toString())
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }catch (e:Exception){
                e.printStackTrace()
                Toast.makeText(context, "Sorry, the Resume URL is not valid.", LENGTH_LONG).show()
            }
        }
        binding.imageViewSeen.setOnClickListener { updateStatus(RecruiterCandidateViewModel.UpdateType.SEEN) }
        binding.imageViewSelected.setOnClickListener { updateStatus(RecruiterCandidateViewModel.UpdateType.SELECTED) }

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(
            RecruiterCandidateViewModel::class.java)
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        recruiterCandidateViewModel.responseGetApplicationById().observe(viewLifecycleOwner, {
            populateViews(it)
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.responseMarkAsSeen().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.responseMarkAsSelected().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_LONG).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Candidate Details"
    }

    private fun populateViews(event: Event<ResponseGetApplicationByIdRecruiter>) {
        binding.apply {
            event.getContentIfNotHandled()?.let { response ->
                if (response.code == 200) {
                    textViewName.text = response.application?.applicant_name
                    textViewCollege.text = response.application?.applicant_collegeName
                    textViewCourse.text = response.application?.applicant_course
                    textViewSemester.text = response.application?.applicant_semester
                    textViewPhone.text = response.application?.applicant_phone
                    textViewPreviousWork.text = response.application?.previousWork
                    textViewResume.text = response.application?.resume
                    response.application?.is_Seen?.let { seen ->
                        if (seen)
                            imageViewSeen.setImageResource(R.drawable.ic_baseline_favorite_24)
                    }
                    response.application?.is_Selected?.let { selected ->
                        if (selected)
                            imageViewSelected.setImageResource(R.drawable.ic_baseline_done_24)
                    }
                } else {
                    Toast.makeText(context, response.message, LENGTH_LONG).show()
                }
                progressCircularLayout.visibility = View.INVISIBLE
            }
        }
    }

    private fun updateStatus(type: RecruiterCandidateViewModel.UpdateType) {
        binding.progressCircularLayout.visibility = View.VISIBLE
        recruiterCandidateViewModel.updateSeenSelected(null, type, null)
    }
}