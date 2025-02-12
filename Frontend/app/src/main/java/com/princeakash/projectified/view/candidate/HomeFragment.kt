package com.princeakash.projectified.view.candidate

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.adapter.HomeAdapter
import com.princeakash.projectified.adapter.HomeAdapter.Companion.AI
import com.princeakash.projectified.adapter.HomeAdapter.Companion.ANDROID
import com.princeakash.projectified.adapter.HomeAdapter.Companion.CONTENT
import com.princeakash.projectified.adapter.HomeAdapter.Companion.ML
import com.princeakash.projectified.adapter.HomeAdapter.Companion.OTHERS
import com.princeakash.projectified.adapter.HomeAdapter.Companion.UIUX
import com.princeakash.projectified.adapter.HomeAdapter.Companion.VIDEO
import com.princeakash.projectified.adapter.HomeAdapter.Companion.WEB
import com.princeakash.projectified.adapter.HomeAdapter.HomeItem
import com.princeakash.projectified.adapter.HomeAdapter.HomeListener
import com.princeakash.projectified.databinding.FragHomeBinding
import com.princeakash.projectified.viewmodel.RecruiterCandidateViewModel


class HomeFragment : Fragment(R.layout.frag_home), HomeListener {

    private lateinit var fragmentBinding: FragHomeBinding
    private lateinit var list: ArrayList<HomeItem>
    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentBinding = FragHomeBinding.bind(view)

        populateRecyclerView()

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(
            RecruiterCandidateViewModel::class.java)

        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        recruiterCandidateViewModel.safeToVisitDomainOffers().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let{safeToVisit->
                if(safeToVisit){
                    findNavController().navigate(R.id.home_to_offers_by_domain)
                }
            }
        })
    }

    private fun populateRecyclerView() {
        list = ArrayList()
        list.add(HomeItem("App Development", R.mipmap.app_dev,ANDROID))
        list.add(HomeItem("Web Development", R.mipmap.web, WEB))
        list.add(HomeItem("Machine Learning", R.mipmap.ml, ML))
        list.add(HomeItem("Artificial Intelligence", R.mipmap.artificial_intelligence, AI))
        list.add(HomeItem("Content Writing", R.mipmap.content, CONTENT))
        list.add(HomeItem("UI/UX Design", R.mipmap.ui, UIUX))
        list.add(HomeItem("Video Editing", R.mipmap.video, VIDEO))
        list.add(HomeItem("Others", R.mipmap.misc, OTHERS))
        fragmentBinding.recyclerView.adapter = HomeAdapter(list, this)
        fragmentBinding.recyclerView.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Projectified"
    }

    override fun onDomainClick(position: Int) {
        fragmentBinding.progressCircularLayout.visibility = View.VISIBLE
        val domainArg = list[position].domainArg
        recruiterCandidateViewModel.getOffersByDomain(domainArg)
    }
}