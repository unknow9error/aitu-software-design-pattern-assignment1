package kz.aitu.builder;

import java.util.List;
import java.util.Objects;

public final class ReportDirector {
    public Report createStudyReport(ReportBuilder builder) {
        Objects.requireNonNull(builder, "Builder must not be null");
        return builder.title("Builder Pattern Study Report")
                .addParagraph("Purpose", "Construct a complex report step by step")
                .addBulletList("Learning goals", List.of(
                        "Separate construction from representation",
                        "Reuse one recipe for Markdown and HTML",
                        "Reject incomplete reports before publication"))
                .addParagraph("Conclusion", "One construction process can produce different representations")
                .build();
    }
}
