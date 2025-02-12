package com.princeakash.projectified.view.candidate

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.adapter.GetOffersByDomainAdapter
import com.princeakash.projectified.databinding.FragAvailableOffersBinding
import com.princeakash.projectified.viewmodel.RecruiterCandidateViewModel

class GetOffersByDomainFragment : Fragment(R.layout.frag_available_offers) , GetOffersByDomainAdapter.GetOffersListener{

    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel
    private lateinit var binding: FragAvailableOffersBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragAvailableOffersBinding.bind(view)
        binding.buttonAddOffer.setOnClickListener { proceedToAddOffer() }
        binding.recyclerViewOffers.apply{
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
        }

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(
            RecruiterCandidateViewModel::class.java)
        subscribeToObservers()

        if(savedInstanceState==null){
            recruiterCandidateViewModel.nullifySafeToVisitDomainOffers()
        }
    }

    private fun subscribeToObservers() {
        recruiterCandidateViewModel.responseGetOffersByDomain().observe(viewLifecycleOwner, {
            Log.d("ByDomain", "subscribeToObservers: ${it.offers!!.size}")
            binding.recyclerViewOffers.adapter = GetOffersByDomainAdapter(it.offers, this)
            binding.progressCircularLayout.visibility = View.INVISIBLE
        })

        recruiterCandidateViewModel.safeToVisitDomainOfferDetails().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let{safeToVisit->
                if(safeToVisit){
                    findNavController().navigate(R.id.offers_to_view_details)
                }
            }
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{ message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(this@GetOffersByDomainFragment.context, message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Offers"
    }

    override fun onViewDetailsClick(offerId: String) {
        //Populate new fragment with details of offer.
        binding.progressCircularLayout.visibility = View.VISIBLE
        recruiterCandidateViewModel.getOfferById(offerId)
    }

    private fun proceedToAddOffer(){
        findNavController().navigate(R.id.offers_to_add_offer)
    }
}