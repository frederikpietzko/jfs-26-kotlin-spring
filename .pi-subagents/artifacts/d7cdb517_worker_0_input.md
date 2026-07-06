# Task for worker

Task 4: Create Service Layer

## Your Task

Create Spring @Service layer with @Transactional methods:

1. OwnerService - findAll, findById, save, update, delete
2. PetService - findByOwnerId, findById, save, update, delete (nested under owner)
3. VisitService - findByPetId, findById, save, update, delete (nested under owner/pet)
4. SpecialityService - findAll, findById, save, update, delete
5. VetService - findAll, findById, save, update, delete

## Requirements

- All service methods @Transactional
-readOnly = true for GET methods
- Save/update returns DTO via fromEntity()
- Delete returns void
- Handle null cases (return null if entity not found)

## Files to Create

src/main/java/com/example/petclinic/service/

Each service needs repositories injected via constructor.

## After Implementation

1. Run `./mvnw install -DskipTests` - should compile without errors
2. Commit with: "feat: add service layer (Owner, Pet, Visit, Vet, Speciality)"

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