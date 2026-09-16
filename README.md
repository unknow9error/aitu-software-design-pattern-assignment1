# Assignment 1: Builder Pattern

A small Java 17 example for the first Software Design Patterns assignment.
The program builds a plain text study report using one `ReportBuilder` class.

## Run

Open the project in IntelliJ IDEA, choose JDK 17, mark `src/main/java` as
Sources Root, and run `kz.aitu.builder.Main`.

Or compile and run from the repository folder:

```bash
javac --release 17 -d build src/main/java/kz/aitu/builder/*.java
java -cp build kz.aitu.builder.Main
```

No external Java libraries are needed. The demo prints a report built through the
Director and one custom report built directly.

## Four classes

| Class | Pattern role | What it does |
| --- | --- | --- |
| `Report` | Product | Stores the finished title and content |
| `ReportBuilder` | Builder | Adds report parts, checks fields and creates the result |
| `ReportDirector` | Director | Calls the steps for a sample study report |
| `Main` | Client | Creates a builder and prints results |

`ReportBuilder` is an ordinary class with `setTitle()`, `addSection()` and
`build()` methods. No inheritance or additional builder classes are used.

```java
ReportDirector director = new ReportDirector();
Report report = director.createStudyReport(new ReportBuilder());
```

The Director is optional. A client can also call the steps directly:

```java
Report report = new ReportBuilder()
        .setTitle("My homework")
        .addSection("Task", "Practice method chaining")
        .build();
```

`setTitle()` and `addSection()` return `this`, so calls can be chained.
`build()` requires a title and at least one section, then creates a new `Report`.
A fresh builder should be used for each report: `build()` does not clear its data.

## Files

- `src/main/java/kz/aitu/builder/` - the four program classes.
- [PDF report with UML](output/pdf/assignment-1-report.pdf).

This simplified version has one concrete builder. The original assignment requires
an interface or abstract Builder and at least two ConcreteBuilders with different
representations. Those requirements are not met by this version.

The assignment also asks for instructor confirmation of a free topic. That confirmation
and uploading the report/link to Moodle are separate from this repository.
