package com.princeakash.projectified.view.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.princeakash.projectified.databinding.ActivityLoginSignupBinding

class LoginSignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tablayout1, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "LOGIN"
                else -> "SIGNUP"
            }
        }.attach()
    }
}