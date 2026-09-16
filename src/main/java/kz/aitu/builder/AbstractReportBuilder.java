package kz.aitu.builder;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractReportBuilder implements ReportBuilder {
    private String title;
    private final List<ReportSection> sections = new ArrayList<>();

    @Override
    public final ReportBuilder title(String title) {
        this.title = TextValidation.requireSingleLine(title, "Title");
        return this;
    }

    @Override
    public final ReportBuilder addParagraph(String heading, String text) {
        TextValidation.requireSingleLine(text, "Paragraph text");
        sections.add(new ReportSection(heading, ReportSection.Kind.PARAGRAPH, List.of(text)));
        return this;
    }

    @Override
    public final ReportBuilder addBulletList(String heading, List<String> items) {
        sections.add(new ReportSection(heading, ReportSection.Kind.BULLET_LIST, items));
        return this;
    }

    @Override
    public final Report build() {
        if (title == null) {
            throw new IllegalStateException("Report requires a title before build()");
        }
        if (sections.isEmpty()) {
            throw new IllegalStateException("Report requires at least one section before build()");
        }
        return new Report(title, format(), render(title, List.copyOf(sections)));
    }

    protected abstract ReportFormat format();

    protected abstract String render(String title, List<ReportSection> sections);
}
