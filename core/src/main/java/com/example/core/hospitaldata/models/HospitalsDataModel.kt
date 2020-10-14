package com.example.core.hospitaldata.models

data class HospitalsDataModel(
    val id: Long,
    val name: String,
    val address1: String,
    val address2: String,
    val address3: String,
    val city: String,
    val county: String,
    val postCode: String
)
