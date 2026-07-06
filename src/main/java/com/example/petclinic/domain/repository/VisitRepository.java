package com.example.petclinic.domain.repository;

import com.example.petclinic.domain.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {
}
