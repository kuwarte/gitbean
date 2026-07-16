package com.kuwarte.gitbean.core;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Index {

    private static final Path INDEX_FILE = Paths.get(".gitbean", "index");

    public static Map<String, String> read() throws IOException {
        Map<String, String> entries = new LinkedHashMap<>();

        if (!Files.exists(INDEX_FILE)) {
            return entries;
        }

        List<String> lines = Files.readAllLines(INDEX_FILE);

        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }

            String[] parts = line.split(" ", 2);
            String hash = parts[0];
            String filename = parts[1];
            entries.put(filename, hash);
        }

        return entries;
    }

    public static void write(Map<String, String> entries) throws IOException {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : entries.entrySet()) {
            String filename = entry.getKey();
            String hash = entry.getValue();
            sb.append(hash).append(" ").append(filename).append("\n");
        }

        Files.writeString(INDEX_FILE, sb.toString());
    }

    public static void addEntry(String filename, String hash) throws IOException {
        Map<String, String> entries = read();
        entries.put(filename, hash);
        write(entries);
    }

}
