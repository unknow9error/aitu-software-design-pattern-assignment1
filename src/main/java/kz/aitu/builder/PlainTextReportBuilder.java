package kz.aitu.builder;

public class PlainTextReportBuilder extends ReportBuilder {
    @Override
    protected String formatSection(String heading, String text) {
        return heading + "\n" + text + "\n\n";
    }

    @Override
    protected String formatReport(String title, String sections) {
        return title + "\n\n" + sections;
    }
}
