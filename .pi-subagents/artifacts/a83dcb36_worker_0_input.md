# Task for worker

Task 1: Define Domain Entities

## Your Task

Create JPA entities with proper relationships:

1. Owner.java - fields: id, firstName, lastName, address, city, telephone; one-to-many pets
2. PetType.java - enum: CAT, DOG, BIRD, FISH, RABBIT, HAMSTER, OTHER
3. Pet.java - fields: id, name, type (enum), birthDate, owner (FK), visits (one-to-many)
4. Visit.java - fields: id, date, description, pet (FK)
5. Speciality.java - fields: id, name; many-to-many with vets
6. Vet.java - fields: id, firstName, lastName; many-to-many specialties

## Requirements

- Use @Entity annotation
- All String fields need @NotBlank or @NotNull validation annotations
- Proper JPA relationships (@OneToMany, @ManyToOne, @ManyToMany)
- Hibernate cascade and orphanRemoval settings appropriate for parent-child relationship
- Pet has one-to-many Visit; Owner has one-to-many Pet

## Files to Create

src/main/java/com/example/petclinic/domain/entity/

## After Implementation

1. Run `./mvnw install -DskipTests` - should compile without errors
2. Commit with: "feat: add domain entities (Owner, Pet, Visit, Vet, Speciality)"

## Report Back With:
- Status (DONE/BLOCKED/NEEDS_CONTEXT)
- Commits created
- Test results summary

## Acceptance Contract
Acceptance level: checked
Completion is not accepted from prose alone. End with a structured acceptance report.

Criteria:
- criterion-1: Implement the requested change without widening scope

Required evidence: changed-files, tests-added, commands-run, residual-risks, no-staged-files

Finish with a fenced JSON block tagged `acceptance-report` in this shape:
Use empty arrays when no items apply; array fields contain strings unless object entries are shown.
```acceptance-report
{
  "criteriaSatisfied": [
    {
      "id": "criterion-1",
      "status": "satisfied",
      "evidence": "specific proof"
    }
  ],
  "changedFiles": [
    "src/file.ts"
  ],
  "testsAddedOrUpdated": [
    "test/file.test.ts"
  ],
  "commandsRun": [
    {
      "command": "command",
      "result": "passed",
      "summary": "short result"
    }
  ],
  "validationOutput": [
    "validation output or concise summary"
  ],
  "residualRisks": [
    "none"
  ],
  "noStagedFiles": true,
  "diffSummary": "short description of the diff",
  "reviewFindings": [
    "blocker: file.ts:12 - issue found, or no blockers"
  ],
  "manualNotes": "anything else the parent should know"
}
```