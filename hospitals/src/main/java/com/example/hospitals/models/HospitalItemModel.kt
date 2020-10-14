package com.example.hospitals.models

data class HospitalItemModel(
    val name: String,
    val addressLine1: String,
    val addressLine2: String,
    val addressLine3: String,
    val city: String,
    val postCode: String,
    val number: String,
    val website: String,
    val sector: String
)
