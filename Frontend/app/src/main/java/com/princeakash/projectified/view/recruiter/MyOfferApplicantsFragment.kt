package com.princeakash.projectified.view.recruiter

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.FragCandidatesBinding
import com.princeakash.projectified.adapter.MyOfferApplicantsAdapter
import com.princeakash.projectified.viewmodel.RecruiterCandidateViewModel

class MyOfferApplicantsFragment : Fragment(R.layout.frag_candidates), MyOfferApplicantsAdapter.MyOfferApplicantListener {

    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel
    private lateinit var binding: FragCandidatesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragCandidatesBinding.bind(view)
        binding.recyclerViewApplicants.apply{
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            //adapter = MyOfferApplicantsAdapter(this@MyOfferApplicantsFragment)
            setHasFixedSize(true)
        }

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(
            RecruiterCandidateViewModel::class.java)
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        recruiterCandidateViewModel.responseGetOfferApplicants().observe(viewLifecycleOwner, { event ->
            event.peekContent().let{ response ->
                /*
                    Special use of setHandled() -> The first observer should activate nullability.
                    Once we reach ApplicantsFragment, no other observer should let the flow enter
                    here. So we ensure that hasBeenHandled is set to true on first entry (via
                    observer) and subsequent entries (indirectly via refreshApplicants)
                 */
                event.setHandled()

                var list = response.applicants
                if(list == null)
                    list = ArrayList()

                //(binding.recyclerViewApplicants.adapter as MyOfferApplicantsAdapter).applicantList = list
                binding.recyclerViewApplicants.adapter = MyOfferApplicantsAdapter(list, this)
                binding.progressCircularLayout.visibility = View.INVISIBLE
            }
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.responseMarkAsSeen().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.responseMarkAsSelected().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.responseGetApplicationById().observe(viewLifecycleOwner, { response ->
            if(!response.hasBeenHandled && response.peekContent().code == 200){
                findNavController().navigate(R.id.candidates_to_candidate_details)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Candidates"
    }

    override fun onViewDetailsClick(applicationId: String) =
        recruiterCandidateViewModel.getApplicationById(applicationId)

    override fun onUpdateClick(applicationId: String, type: RecruiterCandidateViewModel.UpdateType,
        currentStatus: Boolean) = recruiterCandidateViewModel.updateSeenSelected(applicationId, type, true)
}