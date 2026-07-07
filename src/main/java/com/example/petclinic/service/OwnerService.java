package com.example.petclinic.service;

import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;

    @Transactional(readOnly = true)
    public List<Owner> findAll() {
        log.debug("Fetching all owners");
        List<Owner> owners = ownerRepository.findAll();
        log.debug("Found {} owners", owners.size());
        return owners;
    }

    @Transactional(readOnly = true)
    public Owner findById(Long id) {
        log.debug("Fetching owner with id: {}", id);
        Owner owner = ownerRepository.findById(id).orElse(null);
        if (owner == null) {
            log.warn("Owner not found with id: {}", id);
        }
        return owner;
    }

    @Transactional
    public Owner save(Owner entity) {
        log.debug("Saving owner: {}", entity.getFirstName());
        Owner savedOwner = ownerRepository.save(entity);
        log.info("Owner saved successfully with id: {}", savedOwner.getId());
        return savedOwner;
    }

    @Transactional
    public Owner update(Long id, Owner entity) {
        log.debug("Updating owner with id: {}", id);
        if (!ownerRepository.existsById(id)) {
            log.warn("Owner not found for update with id: {}", id);
            return null;
        }
        entity.setId(id);
        Owner updatedOwner = ownerRepository.save(entity);
        log.info("Owner updated successfully with id: {}", updatedOwner.getId());
        return updatedOwner;
    }

    @Transactional
    public void delete(Long id) {
        log.debug("Deleting owner with id: {}", id);
        ownerRepository.deleteById(id);
        log.info("Owner deleted successfully with id: {}", id);
    }
}
