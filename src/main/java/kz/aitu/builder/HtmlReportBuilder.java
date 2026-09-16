package kz.aitu.builder;

import java.util.List;

public final class HtmlReportBuilder extends AbstractReportBuilder {
    @Override
    protected ReportFormat format() {
        return ReportFormat.HTML;
    }

    @Override
    protected String render(String title, List<ReportSection> sections) {
        String safeTitle = escape(title);
        StringBuilder html = new StringBuilder("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n")
                .append("  <meta charset=\"UTF-8\">\n")
                .append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n")
                .append("  <title>").append(safeTitle).append("</title>\n</head>\n<body>\n")
                .append("<main>\n<h1>").append(safeTitle).append("</h1>\n");
        for (ReportSection section : sections) {
            appendSection(html, section);
        }
        return html.append("</main>\n</body>\n</html>\n").toString();
    }

    private static void appendSection(StringBuilder html, ReportSection section) {
        html.append("<section>\n<h2>").append(escape(section.heading())).append("</h2>\n");
        if (section.kind() == ReportSection.Kind.PARAGRAPH) {
            html.append("<p>").append(escape(section.items().get(0))).append("</p>\n");
        } else {
            html.append("<ul>\n");
            for (String item : section.items()) {
                html.append("  <li>").append(escape(item)).append("</li>\n");
            }
            html.append("</ul>\n");
        }
        html.append("</section>\n");
    }

    private static String escape(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
