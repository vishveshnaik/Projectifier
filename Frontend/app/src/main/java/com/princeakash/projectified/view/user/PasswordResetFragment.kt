package com.princeakash.projectified.view.user

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.FragmentPasswordResetBinding
import com.princeakash.projectified.viewmodel.ProfileViewModel

class PasswordResetFragment : Fragment(R.layout.fragment_password_reset) {

    private lateinit var profileViewModel: ProfileViewModel

    private lateinit var binding: FragmentPasswordResetBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPasswordResetBinding.bind(view)
        binding.buttonSetPassword.setOnClickListener { setNewPassword() }

        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        profileViewModel.responseUpdatePassword().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { responseUpdatePassword ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, responseUpdatePassword.message, LENGTH_LONG).show()
                if(responseUpdatePassword.code == "200"){
                    findNavController().navigate(R.id.reset_to_login)
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

    private fun setNewPassword() {
        binding.apply {
            if (editTextPassword.text.toString().isEmpty()){
                editTextPassword.error = "Enter password."
                return
            }

            if (editTextConfirmPassword.toString().isEmpty()){
                editTextConfirmPassword.error = "Enter password again."
                return
            }

            if(editTextConfirmPassword.toString().isEmpty()){
                editTextConfirmPassword.error = "Passwords don't match."
                return
            }

            progressCircularLayout.visibility = View.VISIBLE
            profileViewModel.updatePassword(newPassword = editTextPassword.text!!.toString())
        }
    }
}