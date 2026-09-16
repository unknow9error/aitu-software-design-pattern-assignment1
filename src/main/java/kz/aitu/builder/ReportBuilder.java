package kz.aitu.builder;

public abstract class ReportBuilder {
    private String title = "";
    private String sections = "";

    public ReportBuilder setTitle(String title) {
        checkText(title, "Title");
        this.title = title;
        return this;
    }

    public ReportBuilder addSection(String heading, String text) {
        checkText(heading, "Heading");
        checkText(text, "Section text");
        sections += formatSection(heading, text);
        return this;
    }

    public Report build() {
        if (title.isBlank()) {
            throw new IllegalStateException("Add a title before building the report");
        }
        if (sections.isEmpty()) {
            throw new IllegalStateException("Add at least one section before building the report");
        }
        return new Report(title, formatReport(title, sections));
    }

    private void checkText(String text, String field) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(field + " must not be empty");
        }
    }

    protected abstract String formatSection(String heading, String text);

    protected abstract String formatReport(String title, String sections);
}
