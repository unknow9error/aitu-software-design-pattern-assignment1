package kz.aitu.builder;

public class Main {
    public static void main(String[] args) {
        ReportDirector director = new ReportDirector();

        Report textReport = director.createStudyReport(new ReportBuilder());

        System.out.println("PLAIN TEXT REPORT:");
        System.out.println(textReport.getContent());

        Report customReport = new ReportBuilder()
                .setTitle("My homework")
                .addSection("Task", "Practice method chaining")
                .build();

        System.out.println("REPORT WITHOUT A DIRECTOR:");
        System.out.println(customReport.getContent());
    }
}
