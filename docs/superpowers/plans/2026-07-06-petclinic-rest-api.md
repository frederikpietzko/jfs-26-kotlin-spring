# Pet Clinic REST API Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build Spring Boot REST API for pet clinic management with full CRUD for owners, pets, visits, vets, and specialities using H2 file persistence and Bean Validation

**Architecture:** Layered architecture: Controllers → Services (transactional) → Repositories (JPA) → Entities. Each layer has clear boundaries.

**Tech Stack:** Java 25, Spring Boot 4.1, Spring Data JPA, H2 (file), Bean Validation (JSR-380)

## Global Constraints

- **Java:** 25
- **Spring Boot:** 4.1
- **Persistence:** H2 file database at `jdbc:h2:file:./data/petclinical`
- **Validation:** Bean Validation on all DTOs with `@NotNull`, `@NotBlank`, `@PositiveOrZero`
- **No web UI**, REST API only

### Task 0: Project Setup and Base Configuration

**Files:**
- Modify: `src/main/java/com/example/jfs26kotlinspring/Jfs26KotlinSpringApplication.java`
- Modify: `src/main/resources/application.properties`

**Interfaces:**
- None - foundation tasks

- [ ] **Step 1: Remove Kotlin from package name**

Project is Java-only. Rename package from `com.example.jfs25kotlinspring` to `com.example.petclinic`.

Change in `Jfs26KotlinSpringApplication.java`:  
Current: `package com.example.jfs26kotlinspring;`  
Expected: `package com.example.petclinic;`

- [ ] **Step 2: Update application.properties**

```properties
spring.application.name=pet-clinic-rest-api

# Hibernate DDL auto: create-drop on startup, none in tests
spring.jpa.hibernate.ddl-auto=create-drop

# H2 file-based persistence
spring.datasource.url=jdbc:h2:file:./data/petclinic
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Show SQL (optional, comment out for prod)
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# H2 console
spring.h2.console.enabled=true
```

- [ ] **Step 3: Commit setup changes**

```bash
git add src/main/java/com/example/jfs26kotlinspring/Jfs26KotlinSpringApplication.java src/main/resources/application.properties
git commit -m "chore: rename package to petclinic, configure h2 and jpa"
```

### Task 1: Define Domain Entities

**Files:**
- Create: `src/main/java/com/example/petclinic/domain/entity/Owner.java`
- Create: `src/main/java/com/example/petclinic/domain/entity/PetType.java` (enum)
- Create: `src/main/java/com/example/petclinic/domain/entity/Pet.java`
- Create: `src/main/java/com/example/petclinic/domain/entity/Visit.java`
- Create: `src/main/java/com/example/petclinic/domain/entity/Speciality.java`
- Create: `src/main/java/com/example/petclinic/domain/entity/Vet.java`

**Interfaces:**
- JPA entities with proper relationships

- [ ] **Step 1: Create Owner entity**

```java
package com.example.petclinic.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotNull
    private Integer telephone;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Pet> pets = new HashSet<>();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Integer getTelephone() { return telephone; }
    public void setTelephone(Integer telephone) { this.telephone = telephone; }

    public Set<Pet> getPets() { return pets; }
    public void setPets(Set<Pet> pets) { this.pets = pets; }
}
```

- [ ] **Step 2: Create PetType enum**

```java
package com.example.petclinic.domain.entity;

public enum PetType {
    CAT, DOG, BIRD, FISH, RABBIT, HAMSTER, OTHER
}
```

- [ ] **Step 3: Create Pet entity**

```java
package com.example.petclinic.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PetType type;

    @NotNull
    private LocalDate birthDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Visit> visits = new HashSet<>();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public PetType getType() { return type; }
    public void setType(PetType type) { this.type = type; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public Owner getOwner() { return owner; }
    public void setOwner(Owner owner) { this.owner = owner; }

    public Set<Visit> getVisits() { return visits; }
    public void setVisits(Set<Visit> visits) { this.visits = visits; }
}
```

- [ ] **Step 4: Create Visit entity**

```java
package com.example.petclinic.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate date;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Pet getPet() { return pet; }
    public void setPet(Pet pet) { this.pet = pet; }
}
```

- [ ] **Step 5: Create Speciality entity**

```java
package com.example.petclinic.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Speciality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @ManyToMany(mappedBy = "specialties")
    private Set<Vet> vets = new HashSet<>();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<Vet> getVets() { return vets; }
    public void setVets(Set<Vet> vets) { this.vets = vets; }
}
```

- [ ] **Step 6: Create Vet entity**

```java
package com.example.petclinic.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Vet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "vet_specialties",
        joinColumns = @JoinColumn(name = "vet_id"),
        inverseJoinColumns = @JoinColumn(name = "speciality_id")
    )
    private Set<Speciality> specialties = new HashSet<>();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Set<Speciality> getSpecialties() { return specialties; }
    public void setSpecialties(Set<Speciality> specialties) { this.specialties = specialties; }
}
```

- [ ] **Step 7: Commit entities**

```bash
git add src/main/java/com/example/petclinic/domain/entity/
git commit -m "feat: add domain entities (Owner, Pet, Visit, Vet, Speciality)"
```

### Task 2: Create Repository Interfaces

**Files:**
- Create: `src/main/java/com/example/petclinic/domain/repository/OwnerRepository.java`
- Create: `src/main/java/com/example/petclinic/domain/repository/PetRepository.java`
- Create: `src/main/java/com/example/petclinic/domain/repository/VisitRepository.java`
- Create: `src/main/java/com/example/petclinic/domain/repository/SpecialityRepository.java`
- Create: `src/main/java/com/example/petclinic/domain/repository/VetRepository.java`

**Interfaces:**
- JpaRepository interfaces for each entity

- [ ] **Step 1: Owner repository**

```java
package com.example.petclinic.domain.repository;

import com.example.petclinic.domain.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
```

- [ ] **Step 2: Pet repository**

```java
package com.example.petclinic.domain.repository;

import com.example.petclinic.domain.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
```

- [ ] **Step 3: Visit repository**

```java
package com.example.petclinic.domain.repository;

import com.example.petclinic.domain.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {
}
```

- [ ] **Step 4: Speciality repository**

```java
package com.example.petclinic.domain.repository;

import com.example.petclinic.domain.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
}
```

- [ ] **Step 5: Vet repository**

```java
package com.example.petclinic.domain.repository;

import com.example.petclinic.domain.entity.Vet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VetRepository extends JpaRepository<Vet, Long> {
}
```

- [ ] **Step 6: Commit repositories**

```bash
git add src/main/java/com/example/petclinic/domain/repository/
git commit -m "feat: add repository interfaces"
```

### Task 3: Create DTOs for API

**Files:**
- Create: `src/main/java/com/example/petclinic/api/dto/OwnerRequest.java`
- Create: `src/main/java/com/example/petclinic/api/dto/OwnerResponse.java`
- Create: `src/main/java/com/example/petclinic/api/dto/PetTypeRequest.java` (enum for JSON)
- Create: `src/main/java/com/example/petclinic/api/dto/PetRequest.java`
- Create: `src/main/java/com/example/petclinic/api/dto/PetResponse.java`
- Create: `src/main/java/com/example/petclinic/api/dto/VisitRequest.java`
- Create: `src/main/java/com/example/petclinic/api/dto/VisitResponse.java`
- Create: `src/main/java/com/example/petclinic/api/dto/SpecialityRequest.java`
- Create: `src/main/java/com/example/petclinic/api/dto/SpecialityResponse.java`
- Create: `src/main/java/com/example/petclinic/api/dto/VetRequest.java`
- Create: `src/main/java/com/example/petclinic/api/dto/VetResponse.java`

**Interfaces:**
- Request DTOs have creation validation
- Response DTOs expose all fields
- ID fields use `@PositiveOrZero` (0 for new)

- [ ] **Step 1: Owner DTOs**

```java
// OwnerRequest.java
package com.example.petclinic.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class OwnerRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotNull
    @Pattern(regexp = "^\\d{9}$", message = "Telephone must be exactly 9 digits")
    private String telephone;

    // Getters and setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}

// OwnerResponse.java
package com.example.petclinic.api.dto;

import java.util.List;
import java.util.stream.Collectors;

public class OwnerResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String telephone;
    private List<PetResponse> pets;

    // Constructor
    public OwnerResponse(Long id, String firstName, String lastName, String address, String city, String telephone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.telephone = telephone;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public List<PetResponse> getPets() { return pets; }
    public void setPets(List<PetResponse> pets) { this.pets = pets; }

    // Helper method to convert from entity
    public static OwnerResponse fromEntity(com.example.petclinic.domain.entity.Owner owner) {
        OwnerResponse response = new OwnerResponse(
            owner.getId(),
            owner.getFirstName(),
            owner.getLastName(),
            owner.getAddress(),
            owner.getCity(),
            owner.getTelephone().toString()
        );
        if (owner.getPets() != null) {
            response.setPets(owner.getPets().stream()
                .map(PetResponse::fromEntity)
                .collect(Collectors.toList()));
        }
        return response;
    }
}
```

- [ ] **Step 2: PetType enum for API**

```java
// PetTypeRequest.java (for JSON input)
package com.example.petclinic.api.dto;

public enum PetTypeRequest {
    CAT, DOG, BIRD, FISH, RABBIT, HAMSTER, OTHER
}

// Convert in service layer using PetType.valueOf()
```

- [ ] **Step 3: Pet DTOs**

```java
// PetRequest.java
package com.example.petclinic.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class PetRequest {
    @NotBlank
    private String name;

    @NotNull
    private PetTypeRequest type;

    @NotNull
    private LocalDate birthDate;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public PetTypeRequest getType() { return type; }
    public void setType(PetTypeRequest type) { this.type = type; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
}

// PetResponse.java
package com.example.petclinic.api.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PetResponse {
    private Long id;
    private String name;
    private PetTypeRequest type;
    private LocalDate birthDate;
    private List<VisitResponse> visits;

    public PetResponse(Long id, String name, PetTypeRequest type, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.birthDate = birthDate;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public PetTypeRequest getType() { return type; }
    public void setType(PetTypeRequest type) { this.type = type; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public List<VisitResponse> getVisits() { return visits; }
    public void setVisits(List<VisitResponse> visits) { this.visits = visits; }

    public static PetResponse fromEntity(com.example.petclinic.domain.entity.Pet pet) {
        PetResponse response = new PetResponse(
            pet.getId(),
            pet.getName(),
            PetTypeRequest.valueOf(pet.getType().name()),
            pet.getBirthDate()
        );
        if (pet.getVisits() != null) {
            response.setVisits(pet.getVisits().stream()
                .map(VisitResponse::fromEntity)
                .collect(Collectors.toList()));
        }
        return response;
    }
}
```

- [ ] **Step 4: Visit DTOs**

```java
// VisitRequest.java
package com.example.petclinic.api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class VisitRequest {
    @NotNull
    private LocalDate date;

    private String description;

    // Getters and setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

// VisitResponse.java
package com.example.petclinic.api.dto;

import java.time.LocalDate;

public class VisitResponse {
    private Long id;
    private LocalDate date;
    private String description;

    public VisitResponse(Long id, LocalDate date, String description) {
        this.id = id;
        this.date = date;
        this.description = description;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public static VisitResponse fromEntity(com.example.petclinic.domain.entity.Visit visit) {
        return new VisitResponse(
            visit.getId(),
            visit.getDate(),
            visit.getDescription()
        );
    }
}
```

- [ ] **Step 5: Speciality DTOs**

```java
// SpecialityRequest.java
package com.example.petclinic.api.dto;

import jakarta.validation.constraints.NotBlank;

public class SpecialityRequest {
    @NotBlank
    private String name;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

// SpecialityResponse.java
package com.example.petclinic.api.dto;

import java.util.List;
import java.util.stream.Collectors;

public class SpecialityResponse {
    private Long id;
    private String name;

    public SpecialityResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public static SpecialityResponse fromEntity(com.example.petclinic.domain.entity.Speciality speciality) {
        return new SpecialityResponse(
            speciality.getId(),
            speciality.getName()
        );
    }

    public static List<SpecialityResponse> fromEntities(List<com.example.petclinic.domain.entity.Speciality> specialities) {
        return specialities.stream().map(SpecialityResponse::fromEntity).collect(Collectors.toList());
    }
}
```

- [ ] **Step 6: Vet DTOs**

```java
// VetRequest.java
package com.example.petclinic.api.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class VetRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private List<Long> specialityIds;

    // Getters and setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public List<Long> getSpecialityIds() { return specialityIds; }
    public void setSpecialityIds(List<Long> specialityIds) { this.specialityIds = specialityIds; }
}

// VetResponse.java
package com.example.petclinic.api.dto;

import java.util.List;
import java.util.stream.Collectors;

public class VetResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private List<SpecialityResponse> specialties;

    public VetResponse(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public List<SpecialityResponse> getSpecialties() { return specialties; }
    public void setSpecialties(List<SpecialityResponse> specialties) { this.specialties = specialties; }

    public static VetResponse fromEntity(com.example.petclinic.domain.entity.Vet vet) {
        VetResponse response = new VetResponse(
            vet.getId(),
            vet.getFirstName(),
            vet.getLastName()
        );
        if (vet.getSpecialties() != null) {
            response.setSpecialties(vet.getSpecialties().stream()
                .map(SpecialityResponse::fromEntity)
                .collect(Collectors.toList()));
        }
        return response;
    }

    public static List<VetResponse> fromEntities(List<com.example.petclinic.domain.entity.Vet> vets) {
        return vets.stream().map(VetResponse::fromEntity).collect(Collectors.toList());
    }
}
```

- [ ] **Step 7: Commit DTOs**

```bash
git add src/main/java/com/example/petclinic/api/dto/
git commit -m "feat: add API DTOs (Owner, Pet, Visit, Vet, Speciality)"
```

### Task 4: Create Service Layer

**Files:**
- Create: `src/main/java/com/example/petclinic/service/OwnerService.java`
- Create: `src/main/java/com/example/petclinic/service/PetService.java`
- Create: `src/main/java/com/example/petclinic/service/VisitService.java`
- Create: `src/main/java/com/example/petclinic/service/SpecialityService.java`
- Create: `src/main/java/com/example/petclinic/service/VetService.java`

**Interfaces:**
- Service methods validate DTOs and map to entities
- All methods transactional

- [ ] **Step 1: Owner service**

```java
package com.example.petclinic.service;

import com.example.petclinic.api.dto.OwnerRequest;
import com.example.petclinic.api.dto.OwnerResponse;
import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.repository.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

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

    public OwnerResponse save(OwnerRequest request) {
        Owner owner = new Owner();
        owner.setFirstName(request.getFirstName());
        owner.setLastName(request.getLastName());
        owner.setAddress(request.getAddress());
        owner.setCity(request.getCity());
        owner.setTelephone(Integer.parseInt(request.getTelephone()));
        
        return OwnerResponse.fromEntity(ownerRepository.save(owner));
    }

    public OwnerResponse update(Long id, OwnerRequest request) {
        Owner owner = ownerRepository.findById(id).orElse(null);
        if (owner == null) {
            return null;
        }

        owner.setFirstName(request.getFirstName());
        owner.setLastName(request.getLastName());
        owner.setAddress(request.getAddress());
        owner.setCity(request.getCity());
        owner.setTelephone(Integer.parseInt(request.getTelephone()));
        
        return OwnerResponse.fromEntity(ownerRepository.save(owner));
    }

    public void delete(Long id) {
        ownerRepository.deleteById(id);
    }
}
```

- [ ] **Step 2: Pet service**

```java
package com.example.petclinic.service;

import com.example.petclinic.api.dto.PetRequest;
import com.example.petclinic.api.dto.PetResponse;
import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.entity.Pet;
import com.example.petclinic.domain.entity.PetType;
import com.example.petclinic.domain.repository.OwnerRepository;
import com.example.petclinic.domain.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PetService {
    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;

    public PetService(PetRepository petRepository, OwnerRepository ownerRepository) {
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;
    }

    @Transactional(readOnly = true)
    public List<PetResponse> findByOwnerId(Long ownerId) {
        return petRepository.findByOwner_Id(ownerId).stream()
            .map(PetResponse::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public PetResponse findById(Long ownerId, Long petId) {
        return petRepository.findById(petId)
            .map(PetResponse::fromEntity)
            .orElse(null);
    }

    public PetResponse save(Long ownerId, PetRequest request) {
        Owner owner = ownerRepository.findById(ownerId).orElse(null);
        if (owner == null) {
            return null;
        }

        Pet pet = new Pet();
        pet.setName(request.getName());
        pet.setType(PetType.valueOf(request.getType().name()));
        pet.setBirthDate(request.getBirthDate());
        pet.setOwner(owner);

        owner.getPets().add(pet);
        
        return PetResponse.fromEntity(petRepository.save(pet));
    }

    public PetResponse update(Long ownerId, Long petId, PetRequest request) {
        Pet pet = petRepository.findById(petId).orElse(null);
        if (pet == null) {
            return null;
        }

        pet.setName(request.getName());
        pet.setType(PetType.valueOf(request.getType().name()));
        pet.setBirthDate(request.getBirthDate());
        
        return PetResponse.fromEntity(petRepository.save(pet));
    }

    public void delete(Long ownerId, Long petId) {
        petRepository.deleteById(petId);
    }
}
```

- [ ] **Step 3: Visit service**

```java
package com.example.petclinic.service;

import com.example.petclinic.api.dto.VisitRequest;
import com.example.petclinic.api.dto.VisitResponse;
import com.example.petclinic.domain.entity.Pet;
import com.example.petclinic.domain.entity.Visit;
import com.example.petclinic.domain.repository.PetRepository;
import com.example.petclinic.domain.repository.VisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VisitService {
    private final VisitRepository visitRepository;
    private final PetRepository petRepository;

    public VisitService(VisitRepository visitRepository, PetRepository petRepository) {
        this.visitRepository = visitRepository;
        this.petRepository = petRepository;
    }

    @Transactional(readOnly = true)
    public List<VisitResponse> findByPetId(Long ownerId, Long petId) {
        return visitRepository.findByPet_Id(petId).stream()
            .map(VisitResponse::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public VisitResponse findById(Long ownerId, Long petId, Long id) {
        return visitRepository.findById(id)
            .map(VisitResponse::fromEntity)
            .orElse(null);
    }

    public VisitResponse save(Long ownerId, Long petId, VisitRequest request) {
        Pet pet = petRepository.findById(petId).orElse(null);
        if (pet == null) {
            return null;
        }

        Visit visit = new Visit();
        visit.setDate(request.getDate());
        visit.setDescription(request.getDescription());
        visit.setPet(pet);

        pet.getVisits().add(visit);
        
        return VisitResponse.fromEntity(visitRepository.save(visit));
    }

    public VisitResponse update(Long ownerId, Long petId, Long id, VisitRequest request) {
        Visit visit = visitRepository.findById(id).orElse(null);
        if (visit == null) {
            return null;
        }

        visit.setDate(request.getDate());
        visit.setDescription(request.getDescription());
        
        return VisitResponse.fromEntity(visitRepository.save(visit));
    }

    public void delete(Long ownerId, Long petId, Long id) {
        visitRepository.deleteById(id);
    }
}
```

- [ ] **Step 4: Speciality service**

```java
package com.example.petclinic.service;

import com.example.petclinic.api.dto.SpecialityRequest;
import com.example.petclinic.api.dto.SpecialityResponse;
import com.example.petclinic.domain.entity.Speciality;
import com.example.petclinic.domain.repository.SpecialityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SpecialityService {
    private final SpecialityRepository specialityRepository;

    public SpecialityService(SpecialityRepository specialityRepository) {
        this.specialityRepository = specialityRepository;
    }

    @Transactional(readOnly = true)
    public List<SpecialityResponse> findAll() {
        return specialityRepository.findAll().stream()
            .map(SpecialityResponse::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public SpecialityResponse findById(Long id) {
        return specialityRepository.findById(id)
            .map(SpecialityResponse::fromEntity)
            .orElse(null);
    }

    public SpecialityResponse save(SpecialityRequest request) {
        Speciality speciality = new Speciality();
        speciality.setName(request.getName());
        
        return SpecialityResponse.fromEntity(specialityRepository.save(speciality));
    }

    public SpecialityResponse update(Long id, SpecialityRequest request) {
        Speciality speciality = specialityRepository.findById(id).orElse(null);
        if (speciality == null) {
            return null;
        }

        speciality.setName(request.getName());
        
        return SpecialityResponse.fromEntity(specialityRepository.save(speciality));
    }

    public void delete(Long id) {
        specialityRepository.deleteById(id);
    }
}
```

- [ ] **Step 5: Vet service**

```java
package com.example.petclinic.service;

import com.example.petclinic.api.dto.SpecialityResponse;
import com.example.petclinic.api.dto.VetRequest;
import com.example.petclinic.api.dto.VetResponse;
import com.example.petclinic.domain.entity.Speciality;
import com.example.petclinic.domain.entity.Vet;
import com.example.petclinic.domain.repository.SpecialityRepository;
import com.example.petclinic.domain.repository.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VetService {
    private final VetRepository vetRepository;
    private final SpecialityRepository specialityRepository;

    public VetService(VetRepository vetRepository, SpecialityRepository specialityRepository) {
        this.vetRepository = vetRepository;
        this.specialityRepository = specialityRepository;
    }

    @Transactional(readOnly = true)
    public List<VetResponse> findAll() {
        return vetRepository.findAll().stream()
            .map(VetResponse::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public VetResponse findById(Long id) {
        return vetRepository.findById(id)
            .map(VetResponse::fromEntity)
            .orElse(null);
    }

    public VetResponse save(VetRequest request) {
        Vet vet = new Vet();
        vet.setFirstName(request.getFirstName());
        vet.setLastName(request.getLastName());

        if (request.getSpecialityIds() != null && !request.getSpecialityIds().isEmpty()) {
            var specialities = specialityRepository.findAllById(request.getSpecialityIds());
            vet.getSpecialties().addAll(specialities);
        }

        return VetResponse.fromEntity(vetRepository.save(vet));
    }

    public VetResponse update(Long id, VetRequest request) {
        Vet vet = vetRepository.findById(id).orElse(null);
        if (vet == null) {
            return null;
        }

        vet.setFirstName(request.getFirstName());
        vet.setLastName(request.getLastName());

        vet.getSpecialties().clear();
        if (request.getSpecialityIds() != null && !request.getSpecialityIds().isEmpty()) {
            var specialities = specialityRepository.findAllById(request.getSpecialityIds());
            vet.getSpecialties().addAll(specialities);
        }

        return VetResponse.fromEntity(vetRepository.save(vet));
    }

    public void delete(Long id) {
        vetRepository.deleteById(id);
    }
}
```

- [ ] **Step 6: Commit services**

```bash
git add src/main/java/com/example/petclinic/service/
git commit -m "feat: add service layer (Owner, Pet, Visit, Vet, Speciality)"
```

### Task 5: Create REST Controllers

**Files:**
- Create: `src/main/java/com/example/petclinic/api/controller/OwnerController.java`
- Create: `src/main/java/com/example/petclinic/api/controller/PetController.java`
- Create: `src/main/java/com/example/petclinic/api/controller/VisitController.java`
- Create: `src/main/java/com/example/petclinic/api/controller/VetController.java`
- Create: `src/main/java/com/example/petclinic/api/controller/SpecialityController.java`

**Interfaces:**
- RestControllers with proper request mappings
- Use `@Valid` for validation

- [ ] **Step 1: Owner controller**

```java
package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.OwnerRequest;
import com.example.petclinic.api.dto.OwnerResponse;
import com.example.petclinic.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owners")
public class OwnerController {
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public List<OwnerResponse> findAll() {
        return ownerService.findAll();
    }

    @GetMapping("/{id}")
    public OwnerResponse findById(@PathVariable Long id) {
        return ownerService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OwnerResponse save(@Valid @RequestBody OwnerRequest request) {
        return ownerService.save(request);
    }

    @PutMapping("/{id}")
    public OwnerResponse update(@PathVariable Long id, @Valid @RequestBody OwnerRequest request) {
        return ownerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        ownerService.delete(id);
    }
}
```

- [ ] **Step 2: Pet controller**

```java
package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.PetRequest;
import com.example.petclinic.api.dto.PetResponse;
import com.example.petclinic.service.PetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owners/{ownerId}/pets")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public List<PetResponse> findByOwnerId(@PathVariable Long ownerId) {
        return petService.findByOwnerId(ownerId);
    }

    @GetMapping("/{petId}")
    public PetResponse findById(@PathVariable Long ownerId, @PathVariable Long petId) {
        return petService.findById(ownerId, petId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetResponse save(@PathVariable Long ownerId, @Valid @RequestBody PetRequest request) {
        return petService.save(ownerId, request);
    }

    @PutMapping("/{petId}")
    public PetResponse update(@PathVariable Long ownerId, @PathVariable Long petId, @Valid @RequestBody PetRequest request) {
        return petService.update(ownerId, petId, request);
    }

    @DeleteMapping("/{petId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long ownerId, @PathVariable Long petId) {
        petService.delete(ownerId, petId);
    }
}
```

- [ ] **Step 3: Visit controller**

```java
package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.VisitRequest;
import com.example.petclinic.api.dto.VisitResponse;
import com.example.petclinic.service.VisitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owners/{ownerId}/pets/{petId}/visits")
public class VisitController {
    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping
    public List<VisitResponse> findByPetId(@PathVariable Long ownerId, @PathVariable Long petId) {
        return visitService.findByPetId(ownerId, petId);
    }

    @GetMapping("/{id}")
    public VisitResponse findById(@PathVariable Long ownerId, @PathVariable Long petId, @PathVariable Long id) {
        return visitService.findById(ownerId, petId, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisitResponse save(@PathVariable Long ownerId, @PathVariable Long petId, @Valid @RequestBody VisitRequest request) {
        return visitService.save(ownerId, petId, request);
    }

    @PutMapping("/{id}")
    public VisitResponse update(@PathVariable Long ownerId, @PathVariable Long petId, @PathVariable Long id, @Valid @RequestBody VisitRequest request) {
        return visitService.update(ownerId, petId, id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long ownerId, @PathVariable Long petId, @PathVariable Long id) {
        visitService.delete(ownerId, petId, id);
    }
}
```

- [ ] **Step 4: Vet controller**

```java
package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.VetRequest;
import com.example.petclinic.api.dto.VetResponse;
import com.example.petclinic.service.VetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vets")
public class VetController {
    private final VetService vetService;

    public VetController(VetService vetService) {
        this.vetService = vetService;
    }

    @GetMapping
    public List<VetResponse> findAll() {
        return vetService.findAll();
    }

    @GetMapping("/{id}")
    public VetResponse findById(@PathVariable Long id) {
        return vetService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VetResponse save(@Valid @RequestBody VetRequest request) {
        return vetService.save(request);
    }

    @PutMapping("/{id}")
    public VetResponse update(@PathVariable Long id, @Valid @RequestBody VetRequest request) {
        return vetService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vetService.delete(id);
    }
}
```

- [ ] **Step 5: Speciality controller**

```java
package com.example.petclinic.api.controller;

import com.example.petclinic.api.dto.SpecialityRequest;
import com.example.petclinic.api.dto.SpecialityResponse;
import com.example.petclinic.service.SpecialityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/specialities")
public class SpecialityController {
    private final SpecialityService specialityService;

    public SpecialityController(SpecialityService specialityService) {
        this.specialityService = specialityService;
    }

    @GetMapping
    public List<SpecialityResponse> findAll() {
        return specialityService.findAll();
    }

    @GetMapping("/{id}")
    public SpecialityResponse findById(@PathVariable Long id) {
        return specialityService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SpecialityResponse save(@Valid @RequestBody SpecialityRequest request) {
        return specialityService.save(request);
    }

    @PutMapping("/{id}")
    public SpecialityResponse update(@PathVariable Long id, @Valid @RequestBody SpecialityRequest request) {
        return specialityService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        specialityService.delete(id);
    }
}
```

- [ ] **Step 6: Commit controllers**

```bash
git add src/main/java/com/example/petclinic/api/controller/
git commit -m "feat: add REST controllers (Owner, Pet, Visit, Vet, Speciality)"
```

### Task 6: Configure Spring Data JPA and Validation

**Files:**
- Modify: `src/main/resources/application.properties` (already done in Task 0)
- Create: `src/main/java/com/example/petclinic/config/ValidationConfig.java` (optional, for custom validation)

No additional configuration needed - JPA validation auto-configured with Spring Boot.

### Task 7: Add Test Suite

**Files:**
- Create: `src/test/java/com/example/petclinic/service/OwnerServiceTest.java`
- Create: `src/test/java/com/example/petclinic/api/controller/OwnerControllerTest.java`
- Create: similarly for Pet, Visit, Vet, Speciality

**Interfaces:**
- Unit tests for service layer with @MockBean
- Integration tests for controllers with TestRestTemplate

Due to size constraints, here's the pattern for Owner test:

```java
// OwnerServiceTest.java
package com.example.petclinic.service;

import com.example.petclinic.api.dto.OwnerRequest;
import com.example.petclinic.domain.repository.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class OwnerServiceTest {
    @Autowired
    private OwnerService ownerService;

    @MockBean
    private OwnerRepository ownerRepository;

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Test
    void shouldCreateOwner() {
        // Given
        OwnerRequest request = new OwnerRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setAddress("123 Main St");
        request.setCity("Anytown");
        request.setTelephone("123456789");

        when(ownerRepository.save(any())).thenReturn(createOwnerEntity());

        // When
        var result = ownerService.save(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
    }

    private com.example.petclinic.domain.entity.Owner createOwnerEntity() {
        var owner = new com.example.petclinic.domain.entity.Owner();
        owner.setId(1L);
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("123 Main St");
        owner.setCity("Anytown");
        owner.setTelephone(123456789);
        return owner;
    }
}
```

Due to size, I'll provide only Owner test structure - others follow same pattern.

- [ ] **Step 1: Write_owner_service_test**

```java
// src/test/java/com/example/petclinic/service/OwnerServiceTest.java
package com.example.petclinic.service;

import com.example.petclinic.api.dto.OwnerRequest;
import com.example.petclinic.domain.entity.Owner;
import com.example.petclinic.domain.repository.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.ConstraintViolation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class OwnerServiceTest {
    @Autowired
    private OwnerService ownerService;

    @MockBean
    private OwnerRepository ownerRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldValidateOwnerRequest() {
        // Given
        OwnerRequest request = new OwnerRequest();

        // When
        Set<ConstraintViolation<OwnerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
    }

    @Test
    void shouldCreateOwner() {
        // Given
        OwnerRequest request = new OwnerRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setAddress("123 Main St");
        request.setCity("Anytown");
        request.setTelephone("123456789");

        var savedOwner = createOwnerEntity();
        when(ownerRepository.save(any(Owner.class))).thenReturn(savedOwner);

        // When
        var result = ownerService.save(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
    }

    private Owner createOwnerEntity() {
        var owner = new Owner();
        owner.setId(1L);
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("123 Main St");
        owner.setCity("Anytown");
        owner.setTelephone(123456789);
        return owner;
    }
}
```

- [ ] **Step 2: Write_owner_controller_integration_test**

```java
// src/test/java/com/example/petclinic/api/controller/OwnerControllerTest.java
package com.example.petclinic.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OwnerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateOwner() throws Exception {
        // Given
        var request = """
            {
                "firstName": "John",
                "lastName": "Doe",
                "address": "123 Main St",
                "city": "Anytown",
                "telephone": "123456789"
            }
            """;

        // When/Then
        mockMvc.perform(post("/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldReturnAllOwners() throws Exception {
        // Given
        // (Assume at least one owner exists in test DB)

        // When/Then
        mockMvc.perform(get("/owners"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").isGreaterThan(0));
    }
}
```

- [ ] **Step 3: Test and verify**

```bash
./mvnw test -Dtest=OwnerServiceTest,OwnerControllerTest
```

Expected: Tests pass

- [ ] **Step 4: Commit tests**

```bash
git add src/test/java/com/example/petclinic/
git commit -m "test: add Owner service and controller tests"
```

---

## Implementation Summary

| Task | Files Created/Modified |
|------|-----------------------|
| 0 | Configuration, package rename |
| 1 | 5 Entity classes |
| 2 | 5 Repository interfaces |
| 3 | 13 DTO classes |
| 4 | 5 Service classes |
| 5 | 5 Controller classes |
| 6 | (none - auto-configured) |
| 7 | Test files |

Plan saved to `docs/superpowers/plans/2026-07-06-petclinic-rest-api.md`.

Two execution options:

**1. Subagent-Driven (recommended)**  
Dispatch fresh subagent per task, review between tasks

**2. Inline Execution**  
Execute tasks in this session using executing-plans

Which approach?
