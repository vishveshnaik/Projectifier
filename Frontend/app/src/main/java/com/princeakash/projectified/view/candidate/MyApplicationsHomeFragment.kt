package com.princeakash.projectified.view.candidate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.adapter.MyApplicationsAdapter
import com.princeakash.projectified.databinding.FragMyApplicationBinding
import com.princeakash.projectified.viewmodel.RecruiterCandidateViewModel

class MyApplicationsHomeFragment: Fragment(R.layout.frag_my_application), MyApplicationsAdapter.MyApplicationsListener {

    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel
    private lateinit var binding: FragMyApplicationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragMyApplicationBinding.bind(view)
        binding.recyclerViewApplications.apply{
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
        }

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(
            RecruiterCandidateViewModel::class.java)
        subscribeToObservers()

        if(savedInstanceState==null){
            binding.progressCircularLayout.visibility = View.VISIBLE
            recruiterCandidateViewModel.getApplicationsByCandidate()
        }
    }

    private fun subscribeToObservers() {
        recruiterCandidateViewModel.responseGetApplicationDetailByIdCandidate().observe(viewLifecycleOwner, { response ->
            binding.progressCircularLayout.visibility = View.INVISIBLE
            if(!response.hasBeenHandled && response.peekContent().code == 200){
                findNavController().navigate(R.id.home_to_application_details)
            }
        })

        recruiterCandidateViewModel.responseGetApplicationsByCandidate().observe(viewLifecycleOwner, { response ->
            binding.progressCircularLayout.visibility = View.INVISIBLE
            var list = response.applications
            if(list == null)
                list = ArrayList()
            binding.recyclerViewApplications.adapter = MyApplicationsAdapter(list, this@MyApplicationsHomeFragment)
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let{ message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(this@MyApplicationsHomeFragment.context, message, LENGTH_LONG).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "My Apps"
    }

    override fun onViewDetailsClick(applicationId: String) =
        recruiterCandidateViewModel.getApplicationDetailByIdCandidate(applicationId)
}