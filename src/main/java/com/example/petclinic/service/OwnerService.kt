package com.example.petclinic.service

import com.example.petclinic.domain.entity.Owner
import com.example.petclinic.domain.repository.OwnerRepository
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Slf4j
@Service
@RequiredArgsConstructor
class OwnerService {
    private val ownerRepository: OwnerRepository? = null

    @Transactional(readOnly = true)
    fun findAll(): MutableList<Owner> {
        log.debug("Fetching all owners")
        val owners = ownerRepository!!.findAll()
        log.debug("Found {} owners", owners.size)
        return owners
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): Owner? {
        log.debug("Fetching owner with id: {}", id)
        val owner = ownerRepository!!.findById(id).orElse(null)
        if (owner == null) {
            log.warn("Owner not found with id: {}", id)
        }
        return owner
    }

    @Transactional
    fun save(entity: Owner): Owner {
        log.debug("Saving owner: {}", entity.firstName)
        val savedOwner = ownerRepository!!.save<Owner>(entity)
        log.info("Owner saved successfully with id: {}", savedOwner.id)
        return savedOwner
    }

    @Transactional
    fun update(id: Long, entity: Owner): Owner? {
        log.debug("Updating owner with id: {}", id)
        if (!ownerRepository!!.existsById(id)) {
            log.warn("Owner not found for update with id: {}", id)
            return null
        }
        entity.id = id
        val updatedOwner = ownerRepository.save<Owner>(entity)
        log.info("Owner updated successfully with id: {}", updatedOwner.id)
        return updatedOwner
    }

    @Transactional
    fun delete(id: Long) {
        log.debug("Deleting owner with id: {}", id)
        ownerRepository!!.deleteById(id)
        log.info("Owner deleted successfully with id: {}", id)
    }
}
