package kz.aitu.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class ReportBuilderTest {
    private static int passed;

    private ReportBuilderTest() {
    }

    public static void main(String[] args) {
        checkContract("Markdown", MarkdownReportBuilder::new);
        checkContract("HTML", HtmlReportBuilder::new);
        test("exact Markdown representation", ReportBuilderTest::markdownRepresentation);
        test("exact HTML representation", ReportBuilderTest::htmlRepresentation);
        test("Markdown treats markup as literal text", ReportBuilderTest::markdownEscaping);
        test("HTML escapes user text", ReportBuilderTest::htmlEscaping);
        test("director works with both representations", ReportBuilderTest::directorRepresentations);
        System.out.println("PASS: " + passed + " tests");
    }

    private static void checkContract(String name, Supplier<ReportBuilder> factory) {
        test(name + ": surrounding whitespace is normalized", () -> {
            Report padded = factory.get().title("  Title\t")
                    .addParagraph("  Intro ", "    Text\t")
                    .addBulletList(" Goals ", List.of("  One ", "\tTwo")).build();
            equal(sample(factory.get()), padded);
        });
        test(name + ": missing title", () -> expect(IllegalStateException.class,
                "requires a title", () -> factory.get().addParagraph("Heading", "Text").build()));
        test(name + ": missing sections", () -> expect(IllegalStateException.class,
                "at least one section", () -> factory.get().title("Title").build()));
        test(name + ": blank title", () -> expect(IllegalArgumentException.class,
                "Title must not be blank", () -> factory.get().title("  ")));
        test(name + ": null title", () -> expect(IllegalArgumentException.class,
                "Title must not be blank", () -> factory.get().title(null)));
        test(name + ": multiline title", () -> expect(IllegalArgumentException.class,
                "single line", () -> factory.get().title("Title\nInjected heading")));
        test(name + ": blank heading", () -> expect(IllegalArgumentException.class,
                "heading must not be blank", () -> factory.get().addParagraph(" ", "Text")));
        test(name + ": null paragraph", () -> expect(IllegalArgumentException.class,
                "Paragraph text must not be blank", () -> factory.get().addParagraph("Heading", null)));
        test(name + ": multiline paragraph", () -> expect(IllegalArgumentException.class,
                "single line", () -> factory.get().addParagraph("Heading", "one\rtwo")));
        test(name + ": empty list", () -> expect(IllegalArgumentException.class,
                "at least one item", () -> factory.get().addBulletList("Heading", List.of())));
        test(name + ": null list", () -> expect(IllegalArgumentException.class,
                "at least one item", () -> factory.get().addBulletList("Heading", null)));
        test(name + ": blank list item", () -> expect(IllegalArgumentException.class,
                "item must not be blank", () -> factory.get().addBulletList("Heading", List.of(" "))));
        test(name + ": null list item", () -> {
            List<String> items = new ArrayList<>();
            items.add(null);
            expect(IllegalArgumentException.class, "item must not be blank",
                    () -> factory.get().addBulletList("Heading", items));
        });
        test(name + ": multiline list item", () -> expect(IllegalArgumentException.class,
                "single line", () -> factory.get().addBulletList("Heading", List.of("one\ntwo"))));
        test(name + ": fluent identity", () -> {
            ReportBuilder builder = factory.get();
            require(builder.title("Title") == builder, "title must return this");
            require(builder.addParagraph("Heading", "Text") == builder, "paragraph must return this");
            require(builder.addBulletList("List", List.of("Item")) == builder, "list must return this");
        });
        test(name + ": defensive list copy", () -> {
            List<String> items = new ArrayList<>(List.of("Original"));
            ReportBuilder builder = factory.get().title("Title").addBulletList("List", items);
            items.set(0, "Changed");
            contains(builder.build().content(), "Original");
            require(!builder.build().content().contains("Changed"), "caller mutated builder state");
        });
        test(name + ": stable snapshots and repeatable build", () -> {
            ReportBuilder builder = factory.get().title("Title").addParagraph("First", "Original");
            Report first = builder.build();
            equal(first, builder.build());
            Report second = builder.title("Updated").addParagraph("Second", "Added").build();
            equal("Title", first.title());
            require(!first.content().contains("Added"), "earlier product changed");
            equal("Updated", second.title());
            contains(second.content(), "Added");
        });
        test(name + ": failed step preserves existing state", () -> {
            ReportBuilder builder = factory.get().title("Title").addParagraph("First", "Original");
            Report original = builder.build();
            expect(IllegalArgumentException.class, "must not be blank", () -> builder.title(" "));
            expect(IllegalArgumentException.class, "must not be blank",
                    () -> builder.addBulletList("Broken", List.of("Valid", " ")));
            equal(original, builder.build());
        });
        test(name + ": Unicode content", () -> {
            Report report = factory.get().title("Оқу есебі").addParagraph("Итог", "Дайын ✓").build();
            contains(report.content(), "Оқу есебі");
            contains(report.content(), "Дайын ✓");
        });
    }

    private static void markdownRepresentation() {
        Report report = sample(new MarkdownReportBuilder());
        equal(ReportFormat.MARKDOWN, report.format());
        equal("md", report.format().extension());
        equal("# Title\n\n## Intro\n\nText\n\n## Goals\n\n- One\n- Two\n\n", report.content());
    }

    private static void htmlRepresentation() {
        Report report = sample(new HtmlReportBuilder());
        equal(ReportFormat.HTML, report.format());
        equal("html", report.format().extension());
        equal("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1">
                  <title>Title</title>
                </head>
                <body>
                <main>
                <h1>Title</h1>
                <section>
                <h2>Intro</h2>
                <p>Text</p>
                </section>
                <section>
                <h2>Goals</h2>
                <ul>
                  <li>One</li>
                  <li>Two</li>
                </ul>
                </section>
                </main>
                </body>
                </html>
                """, report.content());
    }

    private static Report sample(ReportBuilder builder) {
        return builder.title("Title").addParagraph("Intro", "Text")
                .addBulletList("Goals", List.of("One", "Two")).build();
    }

    private static void markdownEscaping() {
        String input = "<script>*bold* &copy; [link](url) `code` \\";
        String escaped = "\\<script\\>\\*bold\\* \\&copy\\; \\[link\\]\\(url\\) \\`code\\` \\\\";
        String content = new MarkdownReportBuilder().title(input)
                .addParagraph(input, input).addBulletList("List", List.of(input)).build().content();
        contains(content, "# " + escaped + "\n");
        contains(content, "## " + escaped + "\n");
        contains(content, "\n" + escaped + "\n");
        contains(content, "- " + escaped + "\n");
    }

    private static void htmlEscaping() {
        String input = "<script>alert(\"x\")</script> & 'quote'";
        String escaped = "&lt;script&gt;alert(&quot;x&quot;)&lt;/script&gt; &amp; &#39;quote&#39;";
        String content = new HtmlReportBuilder().title(input)
                .addParagraph(input, input).addBulletList("List", List.of(input)).build().content();
        contains(content, "<title>" + escaped + "</title>");
        contains(content, "<h1>" + escaped + "</h1>");
        contains(content, "<h2>" + escaped + "</h2>");
        contains(content, "<p>" + escaped + "</p>");
        contains(content, "<li>" + escaped + "</li>");
        require(!content.contains("<script>"), "unescaped HTML element");
    }

    private static void directorRepresentations() {
        ReportDirector director = new ReportDirector();
        Report markdown = director.createStudyReport(new MarkdownReportBuilder());
        Report html = director.createStudyReport(new HtmlReportBuilder());
        equal(markdown.title(), html.title());
        for (String phrase : List.of("Purpose", "Learning goals", "Conclusion",
                "Separate construction from representation")) {
            contains(markdown.content(), phrase);
            contains(html.content(), phrase);
        }
        require(!markdown.content().equals(html.content()), "representations must differ");
    }

    private static void test(String name, Runnable action) {
        try {
            action.run();
            passed++;
            System.out.println("PASS " + name);
        } catch (Throwable error) {
            throw new AssertionError("FAIL " + name, error);
        }
    }

    private static void expect(Class<? extends Throwable> type, String message, Runnable action) {
        try {
            action.run();
        } catch (Throwable error) {
            require(type.isInstance(error), "Expected " + type.getSimpleName() + ", got " + error);
            contains(error.getMessage(), message);
            return;
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }

    private static void contains(String actual, String expected) {
        require(actual.contains(expected), "Missing: " + expected + "\nIn: " + actual);
    }

    private static void equal(Object expected, Object actual) {
        require(expected.equals(actual), "Expected: " + expected + "\nActual: " + actual);
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
