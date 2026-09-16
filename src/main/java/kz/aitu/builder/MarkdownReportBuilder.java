package kz.aitu.builder;

import java.util.List;

public final class MarkdownReportBuilder extends AbstractReportBuilder {
    @Override
    protected ReportFormat format() {
        return ReportFormat.MARKDOWN;
    }

    @Override
    protected String render(String title, List<ReportSection> sections) {
        StringBuilder markdown = new StringBuilder("# ").append(escape(title)).append("\n\n");
        for (ReportSection section : sections) {
            markdown.append("## ").append(escape(section.heading())).append("\n\n");
            for (String item : section.items()) {
                if (section.kind() == ReportSection.Kind.BULLET_LIST) {
                    markdown.append("- ");
                }
                markdown.append(escape(item)).append('\n');
            }
            markdown.append('\n');
        }
        return markdown.toString().stripTrailing() + "\n";
    }

    private static String escape(String text) {
        // CommonMark backslash escapes keep user-supplied punctuation literal.
        return text.replaceAll("([\\p{Punct}])", "\\\\$1");
    }
}
