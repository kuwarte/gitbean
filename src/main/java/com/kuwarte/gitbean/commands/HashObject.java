package com.kuwarte.gitbean.commands;

import com.kuwarte.gitbean.core.ObjectStore;

import java.io.IOException;
import java.nio.file.*;

public class HashObject {

    public static void run(String[] args) {
        boolean write = false;
        String filePath = null;

        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("-w")) {
                write = true;
            } else {
                filePath = args[i];
            }
        }

        if (filePath == null) {
            System.err.println("usage: gitbean hash-object [-w] <file>");
            System.exit(1);
        }

        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
            byte[] fullContent = ObjectStore.buildObjectContent("blob", fileContent);

            String hash;

            if (write) {
                hash = ObjectStore.writeObject(fullContent);
            } else {
                hash = ObjectStore.sha1Hex(fullContent);
            }

            System.out.println(hash);

        } catch (IOException e) {
            System.err.println("fatal: " + e.getMessage());
            System.exit(1);
        }
    }

}
