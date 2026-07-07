package com.example.petclinic.service;

import com.example.petclinic.api.dto.OwnerResponse;
import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;

    @Transactional(readOnly = true)
    public List<OwnerResponse> findAll() {
        return ownerRepository.findAll().stream()
                .map(OwnerResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public OwnerResponse findById(Long id) {
        return ownerRepository.findById(id)
                .map(OwnerResponse::fromEntity)
                .orElse(null);
    }

    @Transactional
    public OwnerResponse save(Owner entity) {
        Owner saved = ownerRepository.save(entity);
        return OwnerResponse.fromEntity(saved);
    }

    @Transactional
    public OwnerResponse update(Long id, Owner entity) {
        if (!ownerRepository.existsById(id)) {
            return null;
        }
        entity.setId(id);
        Owner saved = ownerRepository.save(entity);
        return OwnerResponse.fromEntity(saved);
    }

    @Transactional
    public void delete(Long id) {
        ownerRepository.deleteById(id);
    }
}
