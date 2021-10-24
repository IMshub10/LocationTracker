package com.summer.locationtracker.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class AuthViewModel : ViewModel() {
    val countryCode: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val phoneNumber: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val firstName: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val lastName: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    init {
        countryCode.value = true // +91 is already set
        phoneNumber.value = false
        firstName.value = false
        lastName.value = false
    }

    val fieldsAreValid = FieldsAreValid(countryCode, phoneNumber, firstName, lastName)

    inner class FieldsAreValid(
        countryCode: MutableLiveData<Boolean>,
        phoneNumber: MutableLiveData<Boolean>,
        firstName: MutableLiveData<Boolean>,
        lastName: MutableLiveData<Boolean>
    ) : MediatorLiveData<Boolean>() {
        init {
            addSource(countryCode) { countryCodeValue ->
                value =
                    (countryCodeValue && phoneNumber.value!! && firstName.value!! && lastName.value!!)
            }
            addSource(phoneNumber) { phoneNumberValue ->
                value =
                    (countryCode.value!! && phoneNumberValue && firstName.value!! && lastName.value!!)
            }
            addSource(firstName) { firstNameValue ->
                value =
                    (countryCode.value!! && phoneNumber.value!! && firstNameValue && lastName.value!!)
            }
            addSource(lastName) { lastNameValue ->
                value =
                    (countryCode.value!! && phoneNumber.value!! && firstName.value!! && lastNameValue)
            }
        }
    }
}