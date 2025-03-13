package com.example.composeactivity.data

data class Experience(val title: String, val duration: String)

val experienceList = listOf(
    Experience("Software Engineer - Company A", "2020 - Present"),
    Experience("Mobile Developer - Company B", "2018 - 2020"),
    Experience("Intern - Company C", "2017 - 2018")
)