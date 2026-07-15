package com.kuwarte.gitbean.commands;

import java.io.IOException;
import java.nio.file.*;

public class Init {
    public static void run(String[] args) {
        try {
            Path gitDir = Paths.get(".gitbean");

            if (Files.exists(gitDir)) {
                System.out.println("Reinitialized existing gitbean repository in " + gitDir.toAbsolutePath());
                return;
            }

            Files.createDirectories(gitDir.resolve("objects"));
            Files.createDirectories(gitDir.resolve("refs/heads"));
            Files.createDirectories(gitDir.resolve("refs/tags"));

            Files.writeString(gitDir.resolve("HEAD"), "ref: refs/heads/main\n");

            System.out.println("Initialized empty gitbean repository in " + gitDir.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("fatal: " + e.getMessage());
            System.exit(1);
        }
    }
}
