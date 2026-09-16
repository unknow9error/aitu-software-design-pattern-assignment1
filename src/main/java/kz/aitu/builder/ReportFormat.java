package kz.aitu.builder;

public enum ReportFormat {
    MARKDOWN("md"),
    HTML("html");

    private final String extension;

    ReportFormat(String extension) {
        this.extension = extension;
    }

    public String extension() {
        return extension;
    }
}
