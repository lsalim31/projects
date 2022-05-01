package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Lucas Salim
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String command = args[0];
        if (_commands.contains(command)) {
            isInitialized();
        }
        switch (command) {
        case "init" -> Logic.init();
        case "add" -> Logic.add(args);
        case "commit" -> Logic.commit(args);
        case "checkout" -> Logic.checkout(args);
        case "log" -> Logic.log();
        case "rm" -> Logic.rm(args);
        case "branch" -> Logic.branch(args);
        case "status" -> Logic.status();
        case "global-log" -> Logic.globalLog();
        case "find" -> Logic.find(args);
        case "rm-branch" -> Logic.rmBranch(args);
        case "reset" -> Logic.reset(args);
        case "merge" -> Logic.merge(args);
        case "help" -> displayHelp();
        case "exit" -> exit();
        default -> {
            System.out.println("No command with that name exists.");
            System.exit(0);
        }
        }
    }

    private static void exit() {
        FileUtils.deleteDir(Finder.GITLET_FOLDER);
    }

    private static void displayHelp() {
        String output = "Welcome to Gitlet, the tiny stupid "
                + "version-control system!\n"
                + "Here is a list of commands followed by a brief"
                + " description:\n" + "\n"
                + "     • init: Creates a new Gitlet version-control system in the current directory.\n"
                + "     • add: Adds a copy of the file as it currently exists to the staging area.\n"
                + "     • commit: Saves a snapshot of tracked files in the current commit and" +
                " staging area.\n"
                + "     • checkout: Takes the version of the file as it exists in the head commit.\n"
                + "     • log: Display all commits.\n"
                + "     • rm: Unstage the file if it is currently staged for addition.\n"
                + "     • branch: Creates a new branch with the given name, and points.\n"
                + "     • status: Displays the status of the system. \n"
                + "     • global-log: Like log, except displays information about all commits ever made.\n"
                + "     • find: Prints out the ids of all commits that have the given commit message.\n"
                + "     • rm-branch: Deletes the branch with the given name.\n"
                + "     • reset: Checks out all the files tracked by the given commit.\n"
                + "     • merge: Merges files from the given branch into the current branch.\n"
                + "     • help: Displays a list of commands with their respective description.\n"
                + "     • exit: Terminate our Gitlet version-control system.\n";
        System.out.println(output);
    }

    /** Check if our .gitlet is initialized. */
    public static void isInitialized() {
        if (!Finder.GITLET_FOLDER.exists()) {
            System.out.println("Not in an initialized Gitlet directory");
            System.exit(0);
        }
    }

    /** A list of all our known commands. */
    private static ArrayList<String> _commands =
            new ArrayList<>(Arrays.asList("add", "commit",
                    "checkout", "log", "rm", "branch", "status",
                    "global-log", "find", "rm-branch", "reset",
                    "merge")) {
        };
}


