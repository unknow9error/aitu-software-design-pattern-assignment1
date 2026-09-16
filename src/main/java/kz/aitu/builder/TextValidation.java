package kz.aitu.builder;

final class TextValidation {
    private TextValidation() {
    }

    static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }

    static String requireSingleLine(String value, String field) {
        requireText(value, field);
        if (value.contains("\n") || value.contains("\r")) {
            throw new IllegalArgumentException(field + " must be a single line");
        }
        return value;
    }
}
