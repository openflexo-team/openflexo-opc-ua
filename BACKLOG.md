# openflexo-opc-ua — Backlog

Features and evolutions to develop. Each entry says what problem it solves and when it is done. Bugs that are known and not fixed go to `KNOWN_DEFECTS.md`, not here.

Identifiers are stable and never reused: `OPCUA-F-<n>`. Reference them from commits and from other entries.

Status legend: `TODO` · `IN PROGRESS` · `BLOCKED` · `DONE` · `DEFERRED`.

---

## FML runtime errors

### OPCUA-F-1 — The opc-ua adapter raises typed FML errors instead of returning null after a warning or a printed stack  ·  `DONE`

**GitHub:** [openflexo-team/openflexo-opc-ua#1](https://github.com/openflexo-team/openflexo-opc-ua/issues/1)

**Problem.** Part of `CORE-F-11` phase 6 (`openflexo-core/BACKLOG.md`), done with the other remaining adapters in one batch (asked by the user, 2026-11-16), with the rules settled on the first adapters (xlsx, xml, jdbc, rest, formod, docx): the edition actions caught the failures of their evaluations (four catches printing the stack, or a log), and went on with null after a warning.

**Rules applied (decided with the user on the first adapters).** Evaluations through `FMLEvaluation`; every `execute` / `performExecute` declares `throws FMLExecutionException`; a null receiver, model, document, name or required input is an `FMLNullReferenceError`; a select from a null source is an `FMLNullReferenceError` naming the select; a foreseen failure of the technology an `FMLTechnologyError` with a message of its own; a `ModelDefinitionException` an `FMLInternalError`; a cancelled loading an `FMLCancellation`; a typed failure of `AbstractCreateResource` rethrown unchanged; a log just before a throw removed (the failure is reported once, by the editor); the `System.out` traces of the actions removed. Scope: the paths that FML execution goes through; loading and conversion code left.

**Done (2026-11-16).**
- `GetValue`: a null node is an `FMLNullReferenceError`, a node that is not a variable an `FMLTypeError`, a read refused by the server an `FMLTechnologyError` (null after a warning, or an NPE on a null node).
- `AddOPCNode`: evaluations through `FMLEvaluation`.
- The selects of nodes: a null server is an `FMLNullReferenceError` (an empty result before).

**After review (batch review).** The add action, not implemented, logs a warning instead of the removed `System.out` (it was the only
sign that nothing is added).

**Verified (2026-11-16).** opc-ua-ta-test 8 green; ECJ compiles opc-ua-ta. No new test.
