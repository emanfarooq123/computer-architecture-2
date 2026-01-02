package persistence;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public final class CsvUtil {
    private CsvUtil() {}

    public static List<String[]> read(File file, boolean hasHeader) throws IOException {
        List<String[]> rows = new ArrayList<>();
        if (!file.exists()) return rows;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean skipped = !hasHeader;
            while ((line = br.readLine()) != null) {
                if (!skipped) { skipped = true; continue; }
                rows.add(parse(line));
            }
        }
        return rows;
    }

    public static void write(File file, String[] header, List<String[]> rows) throws IOException {
        Path parent = file.toPath().getParent();
        if (parent != null && !Files.exists(parent)) Files.createDirectories(parent);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            if (header != null) {
                bw.write(String.join(",", header));
                bw.newLine();
            }
            for (String[] row : rows) {
                for (int i = 0; i < row.length; i++) {
                    if (i > 0) bw.write(",");
                    bw.write(escape(row[i]));
                }
                bw.newLine();
            }
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        boolean q = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String v = s.replace("\"", "\"\"");
        return q ? ("\"" + v + "\"") : v;
    }

    private static String[] parse(String line) {
        ArrayList<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (inQuotes) {
                if (ch == '\"') {
                    if (i + 1 < line.length() && line.charAt(i+1) == '\"') {
                        cur.append('\"'); i++;
                    } else inQuotes = false;
                } else cur.append(ch);
            } else {
                if (ch == ',') { out.add(cur.toString()); cur.setLength(0); }
                else if (ch == '\"') inQuotes = true;
                else cur.append(ch);
            }
        }
        out.add(cur.toString());
        return out.toArray(new String[0]);
    }
}
