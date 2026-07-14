package com.example.petclinic.api.dto

@JvmRecord
data class OwnerResponse(
    val id: Long?,
    val firstName: String?,
    val lastName: String?,
    val address: String?,
    val city: String?,
    val telephone: String?,
    val pets: MutableSet<PetResponse?>?
)
