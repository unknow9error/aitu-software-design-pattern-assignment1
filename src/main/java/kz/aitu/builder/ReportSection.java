package kz.aitu.builder;

import java.util.List;
import java.util.Objects;

record ReportSection(String heading, Kind kind, List<String> items) {
    enum Kind {
        PARAGRAPH,
        BULLET_LIST
    }

    ReportSection {
        heading = TextValidation.requireSingleLine(heading, "Section heading");
        Objects.requireNonNull(kind, "Section kind must not be null");
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Section must contain at least one item");
        }
        if (kind == Kind.PARAGRAPH && items.size() != 1) {
            throw new IllegalArgumentException("Paragraph must contain exactly one item");
        }
        items = items.stream()
                .map(item -> TextValidation.requireSingleLine(item, "Section item"))
                .toList();
    }
}
