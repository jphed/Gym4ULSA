package com.ULSACUU.gym4ULSA.chat

data class Coach(
    val name: String,
    val specialty: String,
    val phoneNumber: String
)

// Lista de Coaches
val coachList = listOf(
    Coach(
        name = "Jose Holguin",
        specialty = "Coach de Pesas",
        phoneNumber = "5216145218022"
    ),
    Coach(
        name = "Yajahira Payan",
        specialty = "Nutrición y Cardio",
        phoneNumber = "5216145952333"
    ),
    Coach(
        name = "Jorge Parra",
        specialty = "Ayuda General",
        phoneNumber = "5216141224058"
    )
)