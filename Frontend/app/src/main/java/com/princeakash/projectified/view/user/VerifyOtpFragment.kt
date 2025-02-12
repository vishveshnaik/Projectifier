package com.princeakash.projectified.view.user

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.princeakash.projectified.view.MainActivity
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.FragmentVerifyOtpBinding
import com.princeakash.projectified.viewmodel.ProfileViewModel
import com.princeakash.projectified.view.user.LoginHomeFragment.Companion.USER_NAME
import java.util.concurrent.TimeUnit

class VerifyOtpFragment : Fragment(R.layout.fragment_verify_otp) {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentVerifyOtpBinding

    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var phoneNo: String? = null
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentVerifyOtpBinding.bind(view)
        binding.VerifyButton.setOnClickListener {
            if (binding.editTextOtp.text!!.isNotEmpty()) {
                val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, binding.editTextOtp.text.toString())
                signInWithPhoneAuthCredential(credential)
            } else {
                Toast.makeText(context, "Enter the OTP Received", Toast.LENGTH_SHORT).show()
            }
        }

        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        subscribeToObservers(savedInstanceState)

    }

    private fun subscribeToObservers(savedInstanceState: Bundle?) {
        profileViewModel.bodySignUp().observe(viewLifecycleOwner, {
            if(savedInstanceState==null){
                //First load up, hence safe to send OTP
                phoneNo = "+91" + it.phone
                sendVerificationCodeToTheUser()
            }
        })

        profileViewModel.responseSignUp().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                if (response.code == 200) {
                    profileViewModel.logIn()
                }
            }
        })

        profileViewModel.responseLogin().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                if (response.code != 200) {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                } else {
                    if (response.profileCompleted!!) {
                        //Navigate to main activity
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        //Navigate to CreateProfileActivity
                        val bundle = Bundle()
                        bundle.putString(USER_NAME, response.name)
                        val intent = Intent(requireActivity(), CreateProfileActivity::class.java)
                        intent.putExtra(USER_NAME, bundle)
                        startActivity(intent)
                    }
                    requireActivity().finish()
                }

            }

        })

        profileViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun sendVerificationCodeToTheUser() {
        auth = Firebase.auth
        phoneNo?.let {
            val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(it)
                    .setTimeout(60, TimeUnit.SECONDS)
                    .setCallbacks(callbacks)
                    .setActivity(requireActivity())
                    .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }// OnVerificationStateChangedCallbacks

    private var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            Log.d(TAG, "onVerificationCompleted:$credential")
            Toast.makeText(context, "Verification Completed", Toast.LENGTH_SHORT).show()
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e)
            if (e is FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            } else if (e is FirebaseTooManyRequestsException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
            findNavController().navigate(R.id.verify_otp_to_home)
        }

        override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
        ) {
            Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
            Toast.makeText(context, "Code Sent", Toast.LENGTH_SHORT).show()
            // ...
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth = Firebase.auth
        auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Sign Up Done Successfully", Toast.LENGTH_SHORT).show()
                        profileViewModel.signUp()
                    } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(context, "Incorrect OTP", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}