package kz.aitu.builder;

public class ReportBuilderTest {
    public static void main(String[] args) {
        testPlainText();
        testHtml();
        testFluentSteps();
        testMissingTitle();
        testMissingSection();
        System.out.println("All 5 checks passed");
    }

    private static void testPlainText() {
        ReportDirector director = new ReportDirector();
        Report report = director.createStudyReport(new PlainTextReportBuilder());
        check(report.getTitle().equals("My study report"), "Wrong report title");
        check(report.getContent().contains("Topic\nThe Builder pattern"), "Missing text section");
        check(report.getContent().contains("Result\nThe same steps"), "Missing final section");
    }

    private static void testHtml() {
        Report report = new HtmlReportBuilder()
                .setTitle("A & B")
                .addSection("<Topic>", "<script>example</script>")
                .build();
        check(report.getContent().contains("<h1>A &amp; B</h1>"), "Wrong HTML title");
        check(report.getContent().contains("<h2>&lt;Topic&gt;</h2>"), "Wrong HTML section");
        check(!report.getContent().contains("<script>"), "User text became an HTML tag");
        check(report.getContent().endsWith("</body>\n</html>\n"), "Incomplete HTML document");
    }

    private static void testFluentSteps() {
        ReportBuilder builder = new PlainTextReportBuilder();
        check(builder.setTitle("Homework") == builder, "setTitle must return this");
        check(builder.addSection("Task", "Practice") == builder, "addSection must return this");
        Report report = builder.build();
        check(report.getContent().equals("Homework\n\nTask\nPractice\n\n"), "Wrong custom report");
    }

    private static void testMissingTitle() {
        try {
            new PlainTextReportBuilder().addSection("Task", "Practice").build();
            throw new AssertionError("A report without a title should fail");
        } catch (IllegalStateException exception) {
            check(exception.getMessage().contains("title"), "Unclear error message");
        }
    }

    private static void testMissingSection() {
        try {
            new HtmlReportBuilder().setTitle("Homework").build();
            throw new AssertionError("A report without sections should fail");
        } catch (IllegalStateException exception) {
            check(exception.getMessage().contains("section"), "Unclear error message");
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
