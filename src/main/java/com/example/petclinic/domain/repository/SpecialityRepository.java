package com.example.petclinic.domain.repository;

import com.example.petclinic.domain.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
}
