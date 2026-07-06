# Pet Clinic REST API Design

**Date:** 2026-07-06  
**Status:** Approved  

## Overview

Create Spring Boot REST API for pet clinic management. No web UI, only backend endpoints.

## Constraints

- **Java 25**, Spring Boot 4.1
- **H2 file-based persistence** (`jdbc:h2:file:./data/petclinic`)
- **Bean Validation (JSR-380)** on all DTOs

## Architecture

```
src/main/java/com/example/petclinic/
├── api/               # REST + DTOs
│   ├── dto/          # Request/response with @Valid
│   └── controller/   # RestControllers
├── domain/           # Entities + Repositories  
│   ├── entity/       # @Entity, @Id, @ManyToOne etc.
│   └── repository/   # JpaRepository interfaces
└── service/          # Service layer @Transactional
```

## Entities

| Entity | Fields | Associations |
|--------|--------|--------------|
| Owner | id, firstName, lastName, address, city, telephone | pets (one-to-many) |
| Pet | id, name, birthDate, type (ENUM), owner | visits (one-to-many) |
| Visit | id, date, description, pet | (many-to-one to Pet) |
| Vet | id, firstName, lastName | specialties (many-to-many) |
| Speciality | id, name | vets (many-to-many) |

## API Endpoints

### Owners
```
GET    /owners              → List<OwnerDTO>
GET    /owners/{id}         → OwnerDTO
POST   /owners              → Create (validates)
PUT    /owners/{id}         → Update
DELETE /owners/{id}
```

### Pets (nested under owners)
```
GET    /owners/{ownerId}/pets           → List<PetDTO>
GET    /owners/{ownerId}/pets/{petId}   → PetDTO
POST   /owners/{ownerId}/pets           → Create
PUT    /owners/{ownerId}/pets/{petId}   → Update
DELETE /owners/{ownerId}/pets/{petId}
```

### Visits (nested under pets)
```
GET    /owners/{ownerId}/pets/{petId}/visits      → List<VisitDTO>
GET    /owners/{ownerId}/pets/{petId}/visits/{id} → VisitDTO
POST   /owners/{ownerId}/pets/{petId}/visits      → Create
PUT    /owners/{ownerId}/pets/{petId}/visits/{id} → Update
DELETE /owners/{ownerId}/pets/{petId}/visits/{id}
```

### Vets
```
GET /vets → List<VetDTO>
```

### Specialities
```
GET    /specialities        → List<SpecialityDTO>
POST   /specialities        → Create
PUT    /specialities/{id}   → Update
DELETE /specialities/{id}
```

## Validation Rules (DTOs)

- All String fields: `@NotBlank`, max length 255
- IDs: `@PositiveOrZero`
- Dates: `@FutureOrPresent` where appropriate

## Implementation Plan (to be written)

Will use writing-plans skill to create implementation tasks.
