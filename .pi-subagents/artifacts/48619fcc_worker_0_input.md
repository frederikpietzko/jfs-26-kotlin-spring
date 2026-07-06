# Task for worker

Task 2: Create Repository Interfaces

## Your Task

Create JpaRepository interfaces for each entity:

1. OwnerRepository extends JpaRepository<Owner, Long>
2. PetRepository extends JpaRepository<Pet, Long>
3. VisitRepository extends JpaRepository<Visit, Long>
4. SpecialityRepository extends JpaRepository<Speciality, Long>
5. VetRepository extends JpaRepository<Vet, Long>

## Files to Create

src/main/java/com/example/petclinic/domain/repository/

Note: OwnerRepository needs method `findByOwner_Id(Long ownerId)` for Pet/Visit lookups.

## After Implementation

1. Run `./mvnw install -DskipTests` - should compile without errors
2. Commit with: "feat: add repository interfaces"

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