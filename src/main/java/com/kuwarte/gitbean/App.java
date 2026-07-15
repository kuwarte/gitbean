package com.kuwarte.gitbean;

import com.kuwarte.gitbean.commands.CatFile;
import com.kuwarte.gitbean.commands.HashObject;
import com.kuwarte.gitbean.commands.Init;

public class App {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: gitbean <command> [<args>]");
            System.exit(1);
        }

        String command = args[0];

        switch (command) {
            case "init":
                Init.run(args);
                break;

            case "hash-object":
                HashObject.run(args);
                break;

            case "cat-file":
                CatFile.run(args);
                break;

            default:
                System.err.println("gitbean: '" + command + "' is not a gitbean command.");
                System.exit(1);
        }
    }
}
