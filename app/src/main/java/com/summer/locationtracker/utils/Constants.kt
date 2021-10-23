package com.summer.locationtracker.utils

import java.util.regex.Pattern

object Constants {

    val countryCodePattern = Pattern.compile("^\\+?[0-9]{2,3}")
    val phoneNumberPattern = Pattern.compile("^\\?[6-9][0-9]{9}")
}