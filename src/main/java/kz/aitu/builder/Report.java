package kz.aitu.builder;

import java.util.Objects;

public record Report(String title, ReportFormat format, String content) {
    public Report {
        title = TextValidation.requireSingleLine(title, "Title");
        Objects.requireNonNull(format, "Format must not be null");
        content = TextValidation.requireText(content, "Content");
    }
}
