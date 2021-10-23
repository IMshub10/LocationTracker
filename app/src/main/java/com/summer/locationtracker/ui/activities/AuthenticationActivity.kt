package com.summer.locationtracker.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.summer.locationtracker.databinding.ActivityAuthenticationBinding
import com.summer.locationtracker.utils.Constants

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.etCountryCode.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null){
                    val matcher = Constants.countryCodePattern.matcher(s.toString())
                    if(matcher.matches()){
                        Toast.makeText(this@AuthenticationActivity,"Matches",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        binding.etCountryCode.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null){
                    val matcher = Constants.countryCodePattern.matcher(s.toString())
                    if(matcher.matches()){
                        Toast.makeText(this@AuthenticationActivity,"Matches",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}