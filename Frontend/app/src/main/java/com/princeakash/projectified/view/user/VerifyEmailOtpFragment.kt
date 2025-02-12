package com.princeakash.projectified.view.user

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.FragmentVerifyOtpBinding
import com.princeakash.projectified.viewmodel.ProfileViewModel

class VerifyEmailOtpFragment : Fragment(R.layout.fragment_verify_otp) {

    //View Models and Fun Objects
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentVerifyOtpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentVerifyOtpBinding.bind(view)
        binding.VerifyButton.setOnClickListener { verifyOtp() }

        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        profileViewModel.responseVerifyOtp().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_LONG).show()
                if (response.code == "200"){
                    //TODO: Allowed to update password
                    findNavController().navigate(R.id.verify_to_password_reset)
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

    private fun verifyOtp() {
        binding.apply{
            if (editTextOtp.text.toString().isEmpty() || editTextOtp.text.toString().length<6) {
                editTextOtp.error = "Enter a valid OTP."
                return
            }

            val resetOtp = editTextOtp.text.toString()
            progressCircularLayout.visibility = View.VISIBLE
            profileViewModel.verifyOtp(resetOtp)
        }
    }
}