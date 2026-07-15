package com.kuwarte.gitbean.commands;

import com.kuwarte.gitbean.core.ObjectStore;

import java.io.IOException;

public class CatFile {

    public static void run(String[] args) {
        if (args.length < 3) {
            System.err.println("usage: gitbean cat-file <-p|-t|-s> <hash>");
            System.exit(1);
        }

        String flag = args[1];
        String hash = args[2];

        if (!flag.equals("-p") && !flag.equals("-t") && !flag.equals("-s")) {
            System.err.println("usage gitbean cat-file <-p|-t|-s> <hash>");
            System.exit(1);
        }

        try {
            byte[] rawContent = ObjectStore.readObject(hash);

            int nullIndex = indexOfNullByte(rawContent);

            if (nullIndex == -1) {
                System.err.println("fatal: malformed object " + hash + " (no null separator found)");
                System.exit(1);
            }

            String header = new String(rawContent, 0, nullIndex);

            if (flag.equals("-t")) {
                String type = header.split(" ")[0];
                System.out.println(type);
                return;
            }

            if (flag.equals("-s")) {
                String size = header.split(" ")[1];
                System.out.println(size);
                return;
            }

            // if -p then:
            byte[] content = new byte[rawContent.length - nullIndex - 1];

            System.arraycopy(rawContent, nullIndex + 1, content, 0, content.length);

            System.out.write(content);
            System.out.flush();
        } catch (IOException e) {
            System.err.println("fatal: " + e.getMessage());
            System.exit(1);
        }
    }

    private static int indexOfNullByte(byte[] data) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0) {
                return i;
            }
        }

        return -1;
    }

}
