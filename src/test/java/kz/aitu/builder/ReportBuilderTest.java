package kz.aitu.builder;

public class ReportBuilderTest {
    public static void main(String[] args) {
        testPlainText();
        testEmptyInput();
        testFluentSteps();
        testMissingTitle();
        testMissingSection();
        System.out.println("All 5 checks passed");
    }

    private static void testPlainText() {
        ReportDirector director = new ReportDirector();
        Report report = director.createStudyReport(new ReportBuilder());
        check(report.getTitle().equals("My study report"), "Wrong report title");
        check(report.getContent().contains("Topic\nThe Builder pattern"), "Missing text section");
        check(report.getContent().contains("Result\nA report created step by step"), "Missing final section");
    }

    private static void testEmptyInput() {
        try {
            new ReportBuilder().setTitle("   ");
            throw new AssertionError("An empty title should fail");
        } catch (IllegalArgumentException exception) {
            check(exception.getMessage().contains("Title"), "Unclear error message");
        }
    }

    private static void testFluentSteps() {
        ReportBuilder builder = new ReportBuilder();
        check(builder.setTitle("Homework") == builder, "setTitle must return this");
        check(builder.addSection("Task", "Practice") == builder, "addSection must return this");
        Report report = builder.build();
        check(report.getContent().equals("Homework\n\nTask\nPractice\n\n"), "Wrong custom report");
    }

    private static void testMissingTitle() {
        try {
            new ReportBuilder().addSection("Task", "Practice").build();
            throw new AssertionError("A report without a title should fail");
        } catch (IllegalStateException exception) {
            check(exception.getMessage().contains("title"), "Unclear error message");
        }
    }

    private static void testMissingSection() {
        try {
            new ReportBuilder().setTitle("Homework").build();
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
