# Assignment 1: Builder Pattern

Java 17 study-report generator for AITU Software Design Patterns (ShP-2216).
The same construction steps create two different representations: a Markdown
document with heading/list syntax and a complete HTML document with semantic tags.

## Run

Requires **JDK 17 or newer**. There are no Java dependencies or Maven/Gradle downloads.
From the repository root (Bash, including Git Bash/WSL on Windows):

```bash
bash scripts/run.sh
bash scripts/test.sh
```

The demo prints both reports and writes `output/study-report.md` and
`output/study-report.html`. Open the HTML file in a browser or preview the Markdown.
An optional directory is relative to the repository root:

```bash
bash scripts/run.sh output/demo
```

Scripts respect `JAVA_HOME` and compile with `--release 17 -Xlint:all -Werror`.
The dependency-free test runner reports **43 passing tests** and exits nonzero
on failure. GitHub Actions runs the tests and demo on Java 17.

In IntelliJ IDEA, open the repository, select Project SDK 17, mark
`src/main/java` as Sources Root and `src/test/java` as Test Sources Root,
then run `kz.aitu.builder.Main` or `ReportBuilderTest`.

## Build each representation

```java
ReportDirector director = new ReportDirector();
Report markdown = director.createStudyReport(new MarkdownReportBuilder());
Report html = director.createStudyReport(new HtmlReportBuilder());
```

The Director accepts the interface and knows no format-specific syntax. A custom
report can also be built directly; every construction step returns the same builder:

```java
Report report = new HtmlReportBuilder()
        .title("My study plan")
        .addParagraph("Purpose", "Practice the Builder pattern")
        .addBulletList("Steps", List.of("Read the code", "Run the demo"))
        .build();
```

## Structure and pattern roles

| Role | Class | Responsibility |
| --- | --- | --- |
| Product | `Report` | Immutable title, format and serialized content |
| Builder | `ReportBuilder` | Fluent title, paragraph, list and build operations |
| Shared construction | `AbstractReportBuilder` | State, validation and final product creation |
| ConcreteBuilder | `MarkdownReportBuilder` | Markdown headings, paragraphs and lists |
| ConcreteBuilder | `HtmlReportBuilder` | Complete HTML document and semantic elements |
| Director | `ReportDirector` | Reusable study-report construction sequence |
| Client | `Main` | Choose builders, run the recipe, print and save products |

```text
src/main/java/kz/aitu/builder/   implementation
src/test/java/kz/aitu/builder/   executable regression tests
scripts/                       build, run and test
docs/report.md                 editable report with seven Clean Code examples
docs/uml.puml                   PlantUML class-diagram source
docs/uml.svg                    standalone vector diagram
docs/DEFENSE_RU.md              walkthrough and questions for the defense
output/pdf/assignment-1-report.pdf  generated PDF (tracked explicitly)
examples/                      generated Markdown and HTML examples
```

![Builder class diagram](docs/uml.svg)

## Construction rules

- `build()` requires a title and at least one section; incomplete state produces
  a clear `IllegalStateException`.
- Null/blank input, empty lists and line breaks inside fields produce
  `IllegalArgumentException`. Add another paragraph/list item instead of a line break.
- Input is plain text. Both formats escape punctuation/markup; raw HTML and rich
  Markdown input are intentionally unsupported.
- Leading/trailing whitespace is stripped from fields so indentation cannot
  accidentally turn a Markdown paragraph into a code block.
- Lists are copied at the boundary. A later caller mutation cannot alter the builder.
- `build()` returns an immutable snapshot and does **not** reset the builder.
  Repeated builds are equal until another step is added. Use a **fresh builder**
  for every Director recipe, since sections otherwise accumulate.
- Mutable builders are intended for one caller; they are not thread-safe.
- The abstract base and section types are internal to `kz.aitu.builder`.
  Another format can share them inside that package or implement the public interface.

## Report and defense

[Read the report](docs/report.md) or
[download the PDF](output/pdf/assignment-1-report.pdf).
The PDF contains the introduction, UML, seven annotated Clean Code excerpts,
verification, tradeoffs and repository link.
[Russian defense notes](docs/DEFENSE_RU.md) explain the code and demo.

The assignment asks for instructor confirmation of free topics. This repository
does not establish that confirmation. Moodle upload and the in-class defense are
separate steps; review the implementation and report before submission.
