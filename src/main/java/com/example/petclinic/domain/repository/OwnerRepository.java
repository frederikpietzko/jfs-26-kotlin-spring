package com.example.petclinic.domain.repository;

import com.example.petclinic.domain.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    List<Owner> findByOwnerId(Long ownerId);
}
