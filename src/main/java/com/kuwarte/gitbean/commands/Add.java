package com.kuwarte.gitbean.commands;

import com.kuwarte.gitbean.core.Index;
import com.kuwarte.gitbean.core.ObjectStore;

import java.io.IOException;
import java.nio.file.*;

public class Add {

    public static void run(String[] args) {
        if (args.length < 2) {
            System.err.println("usage: gitbean add <file>");
            System.exit(1);
        }

        String filePath = args[1];

        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
            byte[] fullContent = ObjectStore.buildObjectContent("blob", fileContent);

            String hash = ObjectStore.writeObject(fullContent);

            Index.addEntry(filePath, hash);

            System.out.println("added " + filePath + " (" + hash + ")");

        } catch (IOException e) {
            System.err.println("fatal: " + e.getMessage());
            System.exit(1);
        }

    }
}
