package org.omega.util;

import org.omega.value.Section;

import java.util.List;

public class Package {
    public static Section[] packageSections(List<List<String>> palettes, List<long[]> blockIds) {
        Section[] sections = new Section[palettes.size()];
        for (int i = 0; i < sections.length; i++) {

            if (blockIds.get(i) == null) continue;
            sections[i] = new Section(palettes.get(i), blockIds.get(i));

        }
        return sections;
    }
}
