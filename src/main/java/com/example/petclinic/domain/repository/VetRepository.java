package com.example.petclinic.domain.repository;

import com.example.petclinic.domain.entity.Vet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VetRepository extends JpaRepository<Vet, Long> {
}
