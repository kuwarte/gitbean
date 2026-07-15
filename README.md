# gitbean

A from-scratch, byte-compatible clone of Git's core plumbing commands, built in Java as a learning project.

## Why

Building each command exactly like real Git (same object format, same .git-style layout) to understand version control internals deeply — not just approximate the behavior.

## Build & run

mvn compile
mvn exec:java -Dexec.mainClass="com.kuwarte.gitbean.App" -Dexec.args="<command> <args>"

## Commands implemented

- init
- hash-object [-w] <file>

## Progress

See docs/progress/01.md

## Object format

See docs/object-format/01.md
