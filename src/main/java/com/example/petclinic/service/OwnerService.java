package com.example.petclinic.service;

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
    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Owner findById(Long id) {
        return ownerRepository.findById(id)
                .orElse(null);
    }

    @Transactional
    public Owner save(Owner entity) {
        return ownerRepository.save(entity);
    }

    @Transactional
    public Owner update(Long id, Owner entity) {
        if (!ownerRepository.existsById(id)) {
            return null;
        }
        entity.setId(id);
        return ownerRepository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        ownerRepository.deleteById(id);
    }
}
