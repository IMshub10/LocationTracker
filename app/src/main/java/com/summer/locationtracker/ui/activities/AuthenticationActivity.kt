package com.summer.locationtracker.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.summer.locationtracker.data.local_user_storage.LocalUserDataStorage
import com.summer.locationtracker.data.local_user_storage.PreferenceModel
import com.summer.locationtracker.databinding.ActivityAuthenticationBinding
import com.summer.locationtracker.utils.Constants
import com.summer.locationtracker.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding
    private var mIsValid: Boolean = false
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        initViews()
        observeViewModel()
    }

    private fun observeViewModel() {
        authViewModel.fieldsAreValid.observe(this, { isValid ->
            //Log.e(TAG, isValid.toString())
            mIsValid = isValid
            if (isValid) {
                binding.mbLogin.alpha = 1.0f
            } else {
                binding.mbLogin.alpha = .35f
            }
        })
    }

    private fun initViewModel() {
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    private fun initViews() {
        //listen text change
        binding.etCountryCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    val matcher = Constants.countryCodePattern.matcher(s.trim().toString())
                    authViewModel.countryCode.value = matcher.matches()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.etPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    val matcher = Constants.phoneNumberPattern.matcher(s.trim().toString())
                    authViewModel.phoneNumber.value = matcher.matches()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.etUserFirstName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    authViewModel.firstName.value = s.trim().isNotEmpty()
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })
        binding.etUserLastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    authViewModel.lastName.value = s.trim().isNotEmpty()
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        //click listener
        binding.mbLogin.setOnClickListener {
            if (mIsValid) {
                val prefModel = PreferenceModel(
                    binding.etCountryCode.text!!.trim().toString(),
                    binding.etPhoneNumber.text!!.trim().toString().toLong(),
                    binding.etUserFirstName.text!!.trim().toString(),
                    binding.etUserLastName.text!!.trim().toString(),
                    "0",
                    "0"
                )
                CoroutineScope(Dispatchers.IO).launch {
                    LocalUserDataStorage.setLocalUserData(
                        this@AuthenticationActivity,
                        prefModel
                    )
                    withContext(Dispatchers.Main) {
                        val activityIntent =
                            Intent(this@AuthenticationActivity, MapsActivity::class.java)
                        startActivity(activityIntent)

                    }
                }
            } else {
                Toast.makeText(this, "Fields are invalid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        startActivity(a)
    }

}