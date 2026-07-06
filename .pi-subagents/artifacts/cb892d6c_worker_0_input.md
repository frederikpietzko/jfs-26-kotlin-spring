# Task for worker

Task 0: Project Setup and Base Configuration

## Your Task

1. Rename package from `com.example.jfs26kotlinspring` to `com.example.petclinic`
   - File: src/main/java/com/example/jfs26kotlinspring/Jfs26KotlinSpringApplication.java
   - Change package declaration and class reference

2. Verify application.properties is already configured with:
```
spring.application.name=pet-clinic-rest-api
spring.jpa.hibernate.ddl-auto=create-drop
spring.datasource.url=jdbc:h2:file:./data/petclinic
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.h2.console.enabled=true
```

3. Run `./mvnw install -DskipTests` to verify build works

4. Commit changes with message: "chore: rename package to petclinic, configure h2 and jpa"

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