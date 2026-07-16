# gitbean

A from-scratch, byte-compatible clone of Git's core plumbing commands, built in Java as a learning project.

## Why

Building each command exactly like real Git (same object format, same `.git`-style layout) to understand version control internals deeply — not just approximate the behavior.

## Setup: run gitbean from anywhere (recommended)

Add an alias so you can run `gitbean <command>` like a real installed tool, instead of retyping the full classpath every time.

Check your shell first:

```bash
echo $SHELL
```

Edit `~/.bashrc` (or `~/.zshrc` if using zsh) and add:

```bash
alias gitbean='java -cp ~/code/gitbean/target/classes com.kuwarte.gitbean.App'
```

Reload it:

```bash
source ~/.bashrc
```

Now `gitbean init`, `gitbean add test.txt`, etc. work from any directory.

> Whenever you change the Java source, you still need to recompile first:
>
> ```bash
> cd ~/code/gitbean && mvn compile
> ```

### Without the alias

```bash
mvn compile exec:java -Dexec.mainClass="com.kuwarte.gitbean.App" -Dexec.args="<command> <args>"
```

> Note: always chain `compile` before `exec:java` — Maven won't auto-recompile otherwise, and you'll silently run stale code.

## Testing in a sandbox (important)

Never run gitbean commands directly inside the `gitbean/` project folder — it creates a `.gitbean/` folder and test files that clutter your actual source repo.

Instead, use a separate sandbox folder as a sibling to `gitbean/`:

```
~/code/
├── gitbean/     <- this repo (source code only)
└── sandbox/     <- test playground, separate git repo (or no repo at all)
```

```bash
mkdir -p ~/code/sandbox
cd ~/code/sandbox
echo "hello world" > test.txt
echo "second file" > test2.txt

gitbean init
gitbean hash-object -w test.txt
gitbean cat-file -p <hash>
gitbean add test.txt
cat .gitbean/index
```

Reset the sandbox to a clean slate anytime:

```bash
cd ~/code/sandbox
rm -rf .gitbean test.txt test2.txt
```

## Commands implemented

| Command                   | Description                                              |
| ------------------------- | -------------------------------------------------------- |
| `init`                    | Creates `.gitbean/` repo structure                       |
| `hash-object [-w] <file>` | Hashes a file as a blob; `-w` also stores it             |
| `cat-file -p <hash>`      | Prints an object's content                               |
| `cat-file -t <hash>`      | Prints an object's type                                  |
| `cat-file -s <hash>`      | Prints an object's size                                  |
| `add <file>`              | Hashes + stores a file as a blob, stages it in the index |

## Examples

```bash
gitbean init
gitbean hash-object -w test.txt
gitbean cat-file -p <hash>
gitbean add test.txt
```

## Docs

- [Progress / milestone checklist](docs/PROGRESS.md)
- [Object format reference](docs/OBJECT-FORMAT.md)
- [Learnings log](docs/LEARNINGS.md)
