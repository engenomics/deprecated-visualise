package org.engenomics.visualise;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Utils {
    private static List<ColorIndex> colors;

    public static List<ColorIndex> getColors() throws IOException {
        colors = new LinkedList<>();

        // Analyze file
        try (Stream<String> lines = Files.lines(Paths.get("variations.sgo"), Charset.defaultCharset())) {
            lines.forEachOrdered(Utils::process);
        }

        return colors;
    }

    private static void process(String line) {
        Sewi sewi = getSewi(line);

        if (sewi == null) {
            return;
        }

        int j = 0;
        for (long i = sewi.getStart(); i < sewi.getEnd(); i++) {
            colors.add(new ColorIndex(toColor(sewi.getObservedAllele().charAt(j)), i));

            j++;
        }
    }

    private static Color toColor(char nucleotide) {
        switch (nucleotide) {
            case 'A':
                return Color.RED;
            case 'C':
                return Color.GREEN;
            case 'T':
                return Color.BLUE;
            case 'G':
                return Color.YELLOW;
            default:
                return Color.BLACK;
        }
    }

    /**
     * Parses a variation and returns its start, end, was, and is
     *
     * @param variationString - the variation part of the line
     * @return a Sewi with the start, end, was, and is parsed from the variation string
     */
    private static Sewi getSewi(String variationString) {
        if (variationString.contains("WinStart")) {
            return null;
        }

        variationString = variationString.split("\t")[3];

        System.out.println(variationString);

        String[] varSections = variationString.split(":");

        int chromosome = Integer.parseInt(varSections[0]);

        String locationAndReplacement = varSections[1];

        String replacement = locationAndReplacement.replaceAll("[0-9]", "");

        String[] replacementSections = replacement.split(">");

        String was = "", is = "";

        if (replacementSections.length == 2) { // All good
            was = replacementSections[0];
            is = replacementSections[1];
        } else if (replacementSections.length == 1) { // One is empty
            if (replacement.indexOf(">") == 0) { // Original is empty (plain insertion)
                was = "";
                is = replacementSections[0];
            } else if (replacement.indexOf(">") == 1) { // Variation is empty (plain deletion)
                was = replacementSections[0];
                is = "";
            } else {
                // Should never be here
                System.err.println("Something about the contents of the variation string broke! replacement=" + replacement + ", replacementSections=" + Arrays.toString(replacementSections) + ", was=" + was + ", is=" + is + " (Line " + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")");
            }
        } else {
            // Should never be here
            System.err.println("Something about the contents of the variation string broke! replacement=" + replacement + ", replacementSections=" + Arrays.toString(replacementSections) + ", was=" + was + ", is=" + is + " (Line " + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")");
        }

        String location = locationAndReplacement.replaceAll("[^\\d.]", "");

        int start = Integer.parseInt(location);
        int end = start + (is.length() - was.length()) + 1;

        return new Sewi(start, end, was, is);
    }

    public static List<List<ColorIndex>> getLines(List<ColorIndex> colors) {
        List<List<ColorIndex>> lines = new ArrayList<>();
        lines.add(new ArrayList<>());

        int row = 0;
        for (int i = 0; i < colors.size(); i++) {
            lines.get(row).add(colors.get(i));

            if (i % 70 == 0) {
                lines.add(new ArrayList<>());
                row++;
            }
        }

        return lines;
    }
}
