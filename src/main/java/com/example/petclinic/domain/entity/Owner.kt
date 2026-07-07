package com.example.petclinic.domain.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "owners")
class Owner(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @NotBlank
    var firstName: String? = null,
    @NotBlank
    var lastName: String? = null,
    var address: String? = null,
    var city: String? = null,
    var telephone: String? = null,
    pets: MutableSet<Pet> = mutableSetOf()
) {
    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL], orphanRemoval = true)
    final val pets: Set<Pet> field = pets

    fun addPet(pet: Pet) {
        pets.add(pet)
        pet.owner = this
    }
}
