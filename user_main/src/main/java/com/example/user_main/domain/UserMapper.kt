package com.example.user_main.domain

import android.telephony.PhoneNumberUtils
import androidx.compose.ui.text.intl.Locale
import com.example.comon.UserDto
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun map(dto: UserDto): User =
        User(
            fullName = dto.fullName,
            phoneNumber = "+ ${PhoneNumberUtils.formatNumber(
                    dto . phoneNumber,
            Locale.current.region
        )}" ,
            department = dto.department,
        )
}

