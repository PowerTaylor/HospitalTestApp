package com.example.hospitals.models

data class HospitalViewItemModel(
    val id: Long,
    val name: String,
    val firstLineAddress: String,
    val city: String,
    val postCode: String
)
