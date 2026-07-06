package com.example.petclinic.domain.repository;

import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findAllByOwner(Owner owner);
}
