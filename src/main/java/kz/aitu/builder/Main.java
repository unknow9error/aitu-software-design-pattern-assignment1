package kz.aitu.builder;

public class Main {
    public static void main(String[] args) {
        ReportDirector director = new ReportDirector();

        Report textReport = director.createStudyReport(new PlainTextReportBuilder());
        Report htmlReport = director.createStudyReport(new HtmlReportBuilder());

        System.out.println("PLAIN TEXT REPORT:");
        System.out.println(textReport.getContent());
        System.out.println("HTML REPORT:");
        System.out.println(htmlReport.getContent());

        Report customReport = new PlainTextReportBuilder()
                .setTitle("My homework")
                .addSection("Task", "Practice method chaining")
                .build();

        System.out.println("REPORT WITHOUT A DIRECTOR:");
        System.out.println(customReport.getContent());
    }
}
