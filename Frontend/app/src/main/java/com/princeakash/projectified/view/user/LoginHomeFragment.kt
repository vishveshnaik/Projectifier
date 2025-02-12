package com.princeakash.projectified.view.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.princeakash.projectified.view.MainActivity
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.SigninUserBinding
import com.princeakash.projectified.viewmodel.ProfileViewModel

class LoginHomeFragment : Fragment(R.layout.signin_user) {

    //ViewModels
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: SigninUserBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        subscribeToObservers()

        binding = SigninUserBinding.bind(view)
        binding.LogInButton.setOnClickListener { displayHomeScreen() }
        binding.textViewForgotPassword.setOnClickListener { takeToGenerateOtp() }
    }

    private fun subscribeToObservers() {
        profileViewModel.responseLogin().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                if (response.code != 200) {
                    Toast.makeText(context, response.message, LENGTH_LONG).show()
                } else {
                    
                    if (response.profileCompleted!!)
                        startActivity(Intent(activity, MainActivity::class.java))
                    else
                        startActivity(Intent(activity, CreateProfileActivity::class.java))

                    requireActivity().finish()
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

    private fun displayHomeScreen() {
        binding.apply {
            if (editTextEmail.text.toString().isEmpty()){
                editTextEmail.error = "Enter email."
                return
            }
            if (editTextPassword.text.toString().isEmpty()){
                editTextPassword.error = "Enter password."
                return
            }
            val email = editTextEmail.text!!.toString()
            val password = editTextPassword.text!!.toString()

            progressCircularLayout.visibility = View.VISIBLE
            profileViewModel.logIn(email, password)
        }
    }

    private fun takeToGenerateOtp(){
        findNavController().navigate(R.id.home_to_generate_otp)
    }

    companion object {
        const val USER_NAME = "UserName"
    }
}
