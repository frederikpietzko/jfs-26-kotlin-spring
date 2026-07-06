package com.example.petclinic.api.dto;

import java.util.Set;
import java.util.stream.Collectors;

public class OwnerResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String telephone;
    private Set<PetResponse> pets;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Set<PetResponse> getPets() {
        return pets;
    }

    public void setPets(Set<PetResponse> pets) {
        this.pets = pets;
    }

    public static OwnerResponse fromEntity(com.example.petclinic.domain.entity.Owner owner) {
        OwnerResponse response = new OwnerResponse();
        response.setId(owner.getId());
        response.setFirstName(owner.getFirstName());
        response.setLastName(owner.getLastName());
        response.setAddress(owner.getAddress());
        response.setCity(owner.getCity());
        response.setTelephone(owner.getTelephone());
        if (owner.getPets() != null) {
            response.setPets(owner.getPets().stream()
                    .map(PetResponse::fromEntity)
                    .collect(Collectors.toSet()));
        }
        return response;
    }
}
