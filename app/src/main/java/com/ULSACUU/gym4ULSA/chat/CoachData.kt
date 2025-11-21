package com.ULSACUU.gym4ULSA.chat

import com.ULSACUU.gym4ULSA.R

data class Coach(
    val name: String,
    val specialtyResId: Int,
    val phoneNumber: String
)

// Lista de Coaches
val coachList = listOf(
    Coach(
        name = "Jose Holguin",
        specialtyResId = R.string.specialty_weights_coach,
        phoneNumber = "5216145218022"
    ),
    Coach(
        name = "Yajahira Payan",
        specialtyResId = R.string.specialty_nutrition_cardio,
        phoneNumber = "5216145952333"
    ),
    Coach(
        name = "Jorge Parra",
        specialtyResId = R.string.specialty_general_help,
        phoneNumber = "5216141224058"
    )
)