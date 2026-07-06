All good. The DTOs are created with proper Bean Validation annotations.

---

**DONE**

Changed files: `src/main/java/com/example/petclinic/api/dto/` (12 DTOs)

Validation:
```
./mvnw install -DskipTests → BUILD SUCCESS
```

Commits:
- `4231649 feat: add API DTOs (Owner, Pet, Visit, Vet, Speciality)`
- `38914ab fix: correct OwnerRequest.setLastName typo`