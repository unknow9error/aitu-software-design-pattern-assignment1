package kz.aitu.builder;

public class HtmlReportBuilder extends ReportBuilder {
    @Override
    protected String formatSection(String heading, String text) {
        return "<h2>" + escape(heading) + "</h2>\n"
                + "<p>" + escape(text) + "</p>\n";
    }

    @Override
    protected String formatReport(String title, String sections) {
        return "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>" + escape(title) + "</title>\n</head>\n<body>\n"
                + "<h1>" + escape(title) + "</h1>\n"
                + sections
                + "</body>\n</html>\n";
    }

    private String escape(String text) {
        // Keep characters such as < and > as text, not HTML tags.
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
