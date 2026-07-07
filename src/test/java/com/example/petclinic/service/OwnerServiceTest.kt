package com.example.petclinic.service

import com.example.petclinic.domain.entity.Owner
import com.example.petclinic.domain.repository.OwnerRepository
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jakarta.validation.Validator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
internal class OwnerServiceTest {
    @Autowired
    lateinit var ownerService: OwnerService

    @Autowired
    lateinit var ownerRepository: OwnerRepository

    @Autowired
    lateinit var validator: Validator

    private var owner: Owner? = null

    @BeforeEach
    fun setUp() {
        ownerRepository.deleteAll()
        owner = Owner(
            id = null,
            firstName = "John",
            lastName = "Doe",
            address = "123 Main St",
            city = "Springfield",
            telephone = "123456789"
        )
    }

    @Test
    fun shouldValidateOwnerRequest_emptyDTO_failsValidation() {
        val emptyOwner = Owner()

        val violations = validator.validate<Owner?>(emptyOwner)
        violations.size shouldBe 2
        violations.map { it.propertyPath.toString() } shouldContainAll listOf("firstName", "lastName")
    }

    @Test
    fun shouldFindOwnerById() {
        val saved = ownerService.save(owner)

        val result = ownerService.findById(saved.id)

        result.shouldNotBeNull()
        result.firstName shouldBe "John"
        result.lastName shouldBe "Doe"
    }

    @Test
    fun shouldUpdateOwner() {
        val saved = ownerService.save(owner)
        saved.firstName = "Johnny"
        saved.lastName = "Smith"

        val updated = ownerService.update(saved.id, saved)
        updated.shouldNotBeNull()
        updated.firstName shouldBe "Johnny"
        updated.lastName shouldBe "Smith"
        ownerRepository.count() shouldBe 1
    }

    @Test
    fun shouldDeleteOwner() {
        val saved = ownerService.save(owner)
        val id = saved.id

        ownerService.delete(id)
        ownerService.findById(id).shouldBeNull()
        ownerRepository.count() shouldBe 0
    }

    @Test
    fun shouldCreateOwner() {
        val result = ownerService.save(owner)
        result.shouldNotBeNull()
        result.id.shouldNotBeNull()
        result.firstName shouldBe "John"
        result.lastName shouldBe "Doe"
        ownerRepository.count() shouldBe 1
    }

    @Test
    fun shouldFindAllOwners() {
        ownerService.save(owner)
        val owner2 = Owner(
            firstName = "Jane",
            lastName = "Smith",
            address = "456 Oak Ave",
            city = "Shelbyville",
            telephone = "987654321"
        )
        ownerService.save(owner2)

        val results = ownerService.findAll()

        results.shouldNotBeNull()
        results.size shouldBe 2
        results[0].firstName shouldBe "John"
        results[1].firstName shouldBe "Jane"
    }
}
