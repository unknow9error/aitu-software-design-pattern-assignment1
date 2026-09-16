package kz.aitu.builder;

public class ReportDirector {
    public Report createStudyReport(ReportBuilder builder) {
        return builder.setTitle("My study report")
                .addSection("Topic", "The Builder pattern")
                .addSection("Goal", "Learn to create an object step by step")
                .addSection("Result", "A report created step by step")
                .build();
    }
}
