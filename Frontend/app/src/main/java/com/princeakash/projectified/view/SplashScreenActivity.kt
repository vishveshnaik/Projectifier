package com.princeakash.projectified.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.princeakash.projectified.R
import com.princeakash.projectified.view.user.CreateProfileActivity
import com.princeakash.projectified.view.user.LoginSignupActivity
import com.princeakash.projectified.viewmodel.ProfileViewModel

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        val background = object : Thread() {
            override fun run() {
                try {
                    sleep((3 * 1000).toLong())

                    if (!profileViewModel.loginStatus) {
                        takeToLogin()
                    } else if (profileViewModel.isJWTExpired()) {
                        takeToLogin()
                    } else if (!profileViewModel.profileStatus) {
                        takeToCreateProfile()
                    } else {
                        takeToHome()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        //Start Thread
        background.start()
    }

    fun takeToLogin() {
        startActivity(Intent(this, LoginSignupActivity::class.java))
        finish()

    }

    fun takeToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun takeToCreateProfile(){
        startActivity(Intent(this, CreateProfileActivity::class.java))
        finish()
    }
}