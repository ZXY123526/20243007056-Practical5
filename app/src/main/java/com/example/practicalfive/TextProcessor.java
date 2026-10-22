package com.example.practicalfive;

/**
 * Regular Java helper class (not an Activity, no layout of its own) that
 * validates and normalises municipality names. Keeping the rules here means
 * every screen reuses exactly the same validation logic.
 *
 * @author Zhu Xiyuan (20243007056)
 */
public class TextProcessor {

    /**
     * The supported municipalities, in the same fixed order used by the
     * municipalities_array resource (Beijing, Shanghai, Chongqing, Tianjin).
     */
    private static final String[] SUPPORTED = {
            "Beijing", "Shanghai", "Chongqing", "Tianjin"
    };

    /**
     * Validates and normalises the given input.
     *
     * @param municipality the raw text entered by the user
     * @return the correctly capitalised municipality name, or {@code null} when
     *         the input is null, blank, or not one of the four municipalities.
     *         Matching is case-insensitive ("beijing", "BEIJING" and "Beijing"
     *         all return "Beijing").
     */
    public static String processMunicipality(String municipality) {
        if (municipality == null) {
            return null;
        }

        String input = municipality.trim();
        if (input.isEmpty()) {
            return null;
        }

        for (String name : SUPPORTED) {
            if (name.equalsIgnoreCase(input)) {
                return name;
            }
        }

        // Unsupported name, e.g. Guangzhou.
        return null;
    }
}