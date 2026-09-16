package kz.aitu.builder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) throws IOException {
        Path outputDirectory = args.length == 0 ? Path.of("output") : Path.of(args[0]);
        ReportDirector director = new ReportDirector();
        Report markdown = director.createStudyReport(new MarkdownReportBuilder());
        Report html = director.createStudyReport(new HtmlReportBuilder());
        writeReport(outputDirectory, "study-report", markdown);
        writeReport(outputDirectory, "study-report", html);

        Report custom = new MarkdownReportBuilder()
                .title("Custom report without a Director")
                .addParagraph("Observation", "Fluent construction also works directly in the client")
                .build();
        System.out.println("\nDirect fluent API example:\n" + custom.content());
    }

    private static void writeReport(Path directory, String name, Report report) throws IOException {
        Files.createDirectories(directory);
        Path destination = directory.resolve(name + "." + report.format().extension());
        Files.writeString(destination, report.content(), StandardCharsets.UTF_8);
        System.out.println("Created " + report.format() + " report: " + destination);
        System.out.println(report.content());
    }
}
