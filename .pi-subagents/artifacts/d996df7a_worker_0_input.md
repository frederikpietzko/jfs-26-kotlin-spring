# Task for worker

Task 3: Create DTOs for API

## Your Task

Create Request/Response DTOs with Bean Validation:

### Owner DTOs
- OwnerRequest.java - firstName, lastName, address, city, telephone (9 digits)
- OwnerResponse.java - all fields + list of PetResponse with fromEntity() helper

### Pet DTOs  
- PetTypeRequest.java - enum: CAT, DOG, BIRD, FISH, RABBIT, HAMSTER, OTHER
- PetRequest.java - name, type, birthDate
- PetResponse.java - all fields + list of VisitResponse with fromEntity()

### Visit DTOs
- VisitRequest.java - date (required), description (optional)
- VisitResponse.java - id, date, description with fromEntity()

### Speciality DTOs
- SpecialityRequest.java - name (NotBlank)
- SpecialityResponse.java - id, name with fromEntity() and fromEntities()

### Vet DTOs
- VetRequest.java - firstName, lastName, specialityIds list
- VetResponse.java - id, firstName, lastName + specialties list with fromEntity()

## Files to Create

src/main/java/com/example/petclinic/api/dto/

All String fields need @NotBlank, all numeric fields need @NotNull or @Pattern where required.

## After Implementation

1. Run `./mvnw install -DskipTests` - should compile without errors
2. Commit with: "feat: add API DTOs (Owner, Pet, Visit, Vet, Speciality)"

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