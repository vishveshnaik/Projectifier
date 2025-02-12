package com.princeakash.projectified.view.user

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.FragmentGenerateOtpBinding
import com.princeakash.projectified.viewmodel.ProfileViewModel

class GenerateOtpFragment : Fragment(R.layout.fragment_generate_otp) {

    // ViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentGenerateOtpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        subscribeToObservers()

        binding = FragmentGenerateOtpBinding.bind(view)
        binding.buttonGenerateOtp.setOnClickListener { generateOtp() }
    }

    private fun subscribeToObservers() {
        profileViewModel.responseGenerateOtp().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_LONG).show()
                if (response.code == "200") {
                    findNavController().navigate(R.id.generate_to_verify_otp)
                }
            }
        })

        profileViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, message, LENGTH_LONG).show()
            }
        })
    }

    private fun generateOtp() {
        binding.apply {
            if (editTextEmail.text.toString().isEmpty()){
                editTextEmail.error = "Enter Email"
                return
            }

            progressCircularLayout.visibility = View.VISIBLE
            profileViewModel.generateOtp(email = editTextEmail.text.toString())
        }
    }
}