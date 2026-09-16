package kz.aitu.builder;

import java.util.List;

public interface ReportBuilder {
    ReportBuilder title(String title);

    ReportBuilder addParagraph(String heading, String text);

    ReportBuilder addBulletList(String heading, List<String> items);

    Report build();
}
