# WordFinder
This program find a particular string from random ASCII bytes.

# Requirements
- JDK 11. To install go to "https://www.azul.com/downloads/?version=java-11-lts&package=jdk"

# How to run
Maybe it's require to execute `chmod +x run.sh` before the execution.

- Execute the program `sh run.sh`. It requires JRE 11 or JDK 11.
- To compile and run use `buildAndRun.sh` file.

# Decisions made
- Functional Style Code, it's to increase readability, every lambda function does one thing.
- Using RxJava2, it's very useful to do the heavy part: pool threads, parallelism, thread sync, streams and operator over streams.
- Single Responsibility (S from SOLID principles). One class, one responsibility.
- Open Close (O from SOLID principles). You can create a worker with any source of Observable<Byte>, this time I am using a AsciiRandomStream source.
- I have not written unit test because it's saturday, and I have a lot of thing to do this weekend =).
- I use Spring Boot maven plugin to create single binary to execute the program. This plugin packages all dependencies to create a single file to execute the program.

