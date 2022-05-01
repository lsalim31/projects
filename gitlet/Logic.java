package gitlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

/** A class for all the logic in our gitlet system. Here we see methods
 * for all the commands arguments in the Main program.
 * @author Lucas Salim. */
public class Logic {

    /** Creates a new Gitlet version-control system in the current directory.
     * This system will automatically start with one commit: a commit that
     * contains no files and has the commit message initial commit (just like
     * that, with no punctuation). It will have a single branch: master, which
     * initially points to this initial commit, and master will be the current
     * branch. The timestamp for this initial commit will be 00:00:00 UTC,
     * Thursday, 1 January 1970 in whatever format you choose for dates (this
     * is called "The (Unix) Epoch", represented internally by the time 0.)
     * Since the initial commit in all repositories created by Gitlet will
     * have exactly the same content, it follows that all repositories will
     * automatically share this commit (they will all have the same UID) and
     * all commits in all repositories will trace back to it.*/
    public static void init() {
        FileUtils.createDirectory(Finder.GITLET_FOLDER,
                "A Gitlet version-control system already exists"
                        + " in the current directory.");
        FileUtils.createDirectories(Finder.DIRECTORIES);
        FileUtils.createFiles(Finder.FILES);
        Commit initial = new Commit();
        FileUtils.storeObject(initial, Finder.MASTER);
        FileUtils.storeObject(initial, Finder.COMMIT_FOLDER);
    }

    /** Adds a copy of the file as it currently exists to the staging area
     * (see the description of the commit command). For this reason, adding
     * a file is also called staging the file for addition. Staging an
     * already-staged file overwrites the previous entry in the staging
     * area with the new contents. The staging area should be somewhere
     * in .gitlet. If the current working version of the file is identical
     * to the version in the current commit, do not stage it to be added,
     * and remove it from the staging area if it is already there (as can
     * happen when a file is changed, added, and then changed back). The
     * file will no longer be staged for removal (see gitlet rm), if it was
     * at the time of the command. If the file does not exist, print the error
     * message File does not exist. and exit without changing anything.
     * @param args */
    public static void add(String[] args) {
        FileUtils.checkExistence(args, "File does not exist.");
        Tree files;
        if (!(Finder.ADD.length() == 0)) {
            files = Utils.readObject(Finder.ADD, Tree.class);
        } else {
            files = new Tree();
        }
        String filename = args[1];
        File file = Utils.join(Finder.CWD, filename);
        files.add(file);
        if (Finder.REMOVE.length() != 0) {
            Tree currentREMOVE = Utils.readObject(Finder.REMOVE, Tree.class);
            for (String name : currentREMOVE.getMyFiles().keySet()) {
                if (files.contains(name)) {
                    currentREMOVE.remove(name);
                }
            }
            if (currentREMOVE.getMyFiles().size() != 0) {
                Utils.writeObject(Finder.REMOVE, currentREMOVE);
            } else {
                Commit.clearRemovalArea();
            }
        }
        Commit headCommit = Commit.getHeadCommit();
        if (!headCommit.isInitial()) {
            Tree oldCommitTree = headCommit.getTree();
            Tree newFiles = Tree.compareTrees(files, oldCommitTree);
            files = newFiles;
        }
        if (files.getMyFiles().size() == 0) {
            return;
        }
        Utils.writeObject(Finder.ADD, files);
    }


    /** Saves a snapshot of tracked files in the current commit and
     * staging area, so they can be restored at a later time,
     * creating a new commit. The commit is said to be tracking the
     * saved files. By default, each commits snapshot of files will
     * be exactly the same as its parent commits snapshot of files;
     * it will keep versions of files exactly as they are, and not
     * update them. A commit will only update the contents of
     * files it is tracking that have been staged for addition at the
     * time of commit, in which case the commit will now include the
     * version of the file that was staged instead of the version it
     * got from its parent. A commit will save and start tracking any
     * files that were staged for addition but weren't tracked by its
     * parent. Finally, files tracked in the current commit may be
     * untracked in the new commit as a result being staged for removal
     * by the rm command (below). The bottom line: By default a commit
     * is the same as its parent. Files staged for addition and removal
     * are the updates to the commit. Of course, the date (and likely the
     * message) will also be different from the parent. If no files have
     * been staged, abort. Print the message No changes added to the commit.
     * Every commit must have a non-blank message. If it doesn't, print
     * the error message Please enter a commit message. It is not a
     * failure for tracked files to be missing from the working directory
     * or changed in the working directory. Just ignore everything outside
     * the .gitlet directory entirely.
     * @param args */
    public static void commit(String[] args) {
        if (Finder.ADD.length() == 0 && Finder.REMOVE.length() == 0) {
            System.out.println("No changes added to the commit");
            System.exit(0);
        } else {
            if (args.length == 1 || args[1].equals("")) {
                System.out.println("Please enter a commit message");
                System.exit(0);
            }
            Tree fromRemove;
            Tree fromADD;
            if (Finder.REMOVE.length() == 0) {
                fromRemove = null;
            } else {
                fromRemove = Utils.readObject(Finder.REMOVE, Tree.class);
            }
            if (Finder.ADD.length() == 0) {
                fromADD = null;
            } else {
                fromADD = Utils.readObject(Finder.ADD, Tree.class);
            }
            Commit parentCommit = Commit.getHeadCommit();
            Commit newCommit = Commit.clone(parentCommit);
            newCommit.updateCommit(args[1], fromADD, fromRemove,
                    Utils.sha1(Utils.serialize(parentCommit)));
            FileUtils.cleanDirectory(Branches.getCurrentBranch());
            FileUtils.storeObject(newCommit, Branches.getCurrentBranch());
            FileUtils.storeObject(newCommit, Finder.COMMIT_FOLDER);
        }
    }


    /**Takes the version of the file as it exists in the head commit,
     * the front of the current branch, and puts it in the working directory,
     * overwriting the version of the file that's already there if
     * there is one. The new version of the file is not staged.
     * Any files that are tracked in the current branch but are
     * not present in the checked-out branch are deleted.
     * @param args */
    public static void checkout(String[] args) {
        Tree myFiles = null;
        String filename = null;
        if ((args.length == 3 && !args[1].equals("--"))
                || (args.length == 4 && !args[2].equals("--"))) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        if (args[1].equals("--")) {
            filename = args[2];
            Commit headCommit = Commit.getHeadCommit();
            myFiles = headCommit.getTree();
        } else if (args.length == 4) {
            if (args[3].equals("reset case")) {
                Commit thisCommit = Commit.getCommit(args[1]);
                myFiles = thisCommit.getTree();
                for (String file : myFiles.getMyFiles().keySet()) {
                    if (willBeOverwritten(file, myFiles)) {
                        checkFile(file, Commit.getHeadCommit());
                        recoverBlob(myFiles, file);
                    }
                }
                return;
            }
            filename = args[3];
            Commit thisCommit = Commit.getCommit(args[1]);
            myFiles = thisCommit.getTree();
        } else if (args.length == 2) {
            Commit currentCommit = Branches.getCurrentBranchCommit();
            Commit branchCommit = Branches.getCommitFromBranch(args[1]);
            myFiles = branchCommit.getTree();
            if (myFiles != null) {
                for (String file : myFiles.getMyFiles().keySet()) {
                    if (willBeOverwritten(file, myFiles)) {
                        checkFile(file, currentCommit);
                    }
                    recoverBlob(myFiles, file);
                }
            }
            if (currentCommit.getTree() != null) {
                for (String file
                        :currentCommit.getTree().getMyFiles().keySet()) {
                    if (Utils.join(Finder.CWD, file).exists()
                            && !checkEquality(file, branchCommit.getTree())) {
                        Utils.join(Finder.CWD, file).delete();
                    }
                }
            }
            Branches.updateBranch(args[1]);
            return;
        }
        recoverBlob(myFiles, filename);
    }

    /** A helper method. This consists of overwriting the file
     * FILENAME that should live under MYFILES (its Tree) so that
     * a file with the same name in CWD will be this new file.
     * @param myFiles
     * @param filename*/
    public static void recoverBlob(Tree myFiles, String filename) {
        if (!myFiles.contains(filename)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        String blobName = myFiles.get(filename);
        File prev = Utils.join(Finder.BLOBS_FOLDER, blobName);
        Blob last = Utils.readObject(prev, Blob.class);
        byte[] contentLast = last.getContent();
        Utils.writeContents(Utils.join(Finder.CWD, filename),
                contentLast);
    }

    /** Checks whether working FILE is untracked in CURRENTCOMMIT,
     *  given that it would be overwritten by the checkout. If it
     *  is, print There is an untracked file in the way; delete it,
     *  or add and commit it first. and exit. A helper method for
     *  checkout.
     *  @param file
     *  @param currentCommit */
    private static void checkFile(String file, Commit currentCommit) {
        File thisFile = Utils.join(Finder.CWD, file);
        if (thisFile.exists()) {
            if (!checkEquality(file, currentCommit.getTree())) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                System.exit(0);
            }
        }
    }

    /** Assuming we called checkout for a given branch that has SOMETREE for
     * storing our files, check if such file would actually be overwritten;
     * that is: returns true if and only if FILE in SOMETREE is different than
     * file in CWD.
     * @param file
     * @param someTree */
    public static boolean willBeOverwritten(String file, Tree someTree) {
        File thisFile = Utils.join(Finder.CWD, file);
        if (!thisFile.exists()) {
            return false;
        } else {
            String blobName = someTree.get(file);
            return !blobName.equals(Utils.sha1(
                    Utils.serialize(new Blob(thisFile))));
        }
    }


    /** Starting at the current head commit, display information about
     *  each commit backwards along the commit Tree until the initial
     *  commit, following the first parent commit links, ignoring any
     *  second parents found in merge commits. (In regular Git, this
     *  is what you get with git log --first-parent). This set of commit
     *  nodes is called the commit's history. For every node in this
     *  history, the information it should display is the commit id,
     *  the time the commit was made, and the commit message. */
    public static void log() {
        String output = "";
        Commit prevCommit = Commit.getHeadCommit();
        while (!prevCommit.isInitial()) {
            output += toStringCommit(prevCommit);
            prevCommit = Commit.getCommit(prevCommit.getParent());
        }
        output += toStringCommit(prevCommit);
        System.out.print(output);
    }

    /** A helper method for log(). Given a Commit C, this method returns the
     * string version (formatted accordingly) of C.
     * @param c */
    public static String toStringCommit(Commit c) {
        String id = Utils.sha1(Utils.serialize(c));
        String pattern = "EEE MMM d HH:mm:ss yyyy Z";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(c.getDate());
        String message = c.getMessage();
        return "===\n" + "commit " + id + "\n"
                + "Date: " + date + "\n" + message + "\n" + "\n";
    }


    /** Unstage the file if it is currently staged for addition. If the file
     *  is tracked in the current commit, stage it for removal and remove
     * the file from the working directory if the user has not already done
     * so (do not remove it unless it is tracked in the current commit). If
     * the file is neither staged nor tracked by the head commit, print the
     * error message No reason to remove the file.
     * @param args */
    public static void rm(String[] args) {
        if (!(Finder.ADD.length() == 0)) {
            Tree currentStagingTree = Utils.readObject(Finder.ADD,
                    Tree.class);
            if (currentStagingTree.contains(args[1])) {
                if (checkEquality(args[1], currentStagingTree)) {
                    Commit.clearStagingArea();
                    currentStagingTree.getMyFiles().remove(args[1]);
                    Utils.writeObject(Finder.ADD, currentStagingTree);
                }
                return;
            }
        }
        Commit headCommit = Commit.getHeadCommit();
        Tree headTree = headCommit.getTree();
        if (headTree != null && headTree.contains(args[1])) {
            Tree files;
            if (Finder.REMOVE.length() != 0) {
                files = Utils.readObject(Finder.REMOVE, Tree.class);
            } else {
                files = new Tree();
            }
            File file = Utils.join(Finder.BLOBS_FOLDER,
                    headTree.get(args[1]));
            String blobName = file.getName();
            File fileCWD = Utils.join(Finder.CWD, args[1]);
            if (fileCWD.exists()) {
                fileCWD.delete();
                files.getMyFiles().put(fileCWD.getName(), blobName);
            } else {
                files.getMyFiles().put(args[1], file.getName());
            }
            Utils.writeObject(Finder.REMOVE, files);
            return;
        }
        System.out.println("No reason to remove the file.");
        System.exit(0);
    }



    /** Returns true if and only if the file in CWD with name FILENAME
     * is the same as the one in CHECKTREE. A helper method.
     * @param filename
     * @param checkTree */
    public static boolean checkEquality(String filename, Tree checkTree) {
        String blob1Name;
        String blob2Name;
        File thisFile = Utils.join(Finder.CWD, filename);
        if (!thisFile.exists()) {
            return false;
        }
        Blob thisBlob = new Blob(thisFile);
        blob1Name = Utils.sha1(Utils.serialize(thisBlob));
        if (checkTree == null) {
            return false;
        } else {
            blob2Name = checkTree.get(filename);
        }
        return blob1Name.equals(blob2Name);
    }



    /**Creates a new branch with the given name, and points
     * it at the current head node. A branch is nothing more
     * than a name for a reference (a SHA-1 identifier) to a commit
     * node. This command does NOT immediately switch to the newly
     * created branch (just as in real Git). Before you ever call
     * branch, your code should be running with a default branch called
     * "master".
     * @param args */
    public static void branch(String[] args) {
        File newBranch = Utils.join(Finder.BRANCHES_FOLDER, args[1]);
        if (newBranch.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        FileUtils.createDirectory(newBranch);
        Commit currentCommit = Commit.getHeadCommit();
        FileUtils.storeObject(currentCommit, newBranch);
    }




    /** Displays what branches currently exist, and marks the current
     * branch with a *. Also displays what files have been staged for
     * addition or removal. */
    public static void status() {

        File[] b = Finder.BRANCHES_FOLDER.listFiles();
        ArrayList<String> bNames = new ArrayList<>();
        for (File f : b) {
            if (!f.getName().equals("CURRENT")) {
                bNames.add(f.getName());
            }
        }
        Collections.sort(bNames);
        String branches = "";
        for (String name: bNames) {
            if (name.equals(Branches.getCurrentBranch()
                    .getName())) {
                branches += "*" + name + "\n";
            } else {
                branches += name + "\n";
            }
        }
        String adds = "";
        String removes = "";
        if (!(Finder.ADD.length() == 0)) {
            Tree filesADD = Utils.readObject(Finder.ADD, Tree.class);
            ArrayList<String> myADDfilesNames = new ArrayList<>();
            for (String name : filesADD.getMyFiles().keySet()) {
                myADDfilesNames.add(name);
            }
            myADDfilesNames.sort(String::compareTo);
            adds = "";
            for (String name : myADDfilesNames) {
                adds += name + "\n";
            }
        }
        if (!(Finder.REMOVE.length() == 0)) {
            Tree filesREMOVE = Utils.readObject(Finder.REMOVE, Tree.class);
            ArrayList<String> myREMOVEfilesNames = new ArrayList<>();
            for (String name : filesREMOVE.getMyFiles().keySet()) {
                myREMOVEfilesNames.add(name);
            }
            myREMOVEfilesNames.sort(String::compareTo);
            removes = "";
            for (String name : myREMOVEfilesNames) {
                removes += name + "\n";
            }
        }

        String result = "=== Branches ===\n" + branches + "\n"
                + "=== Staged Files ===\n" + adds + "\n"
                + "=== Removed Files ===\n" + removes + "\n"
                + "=== Modifications Not Staged For Commit ===\n"
                + "\n" + "=== Untracked Files ===\n";
        System.out.println(result);
    }


    /** Like log, except displays information about all commits ever made.
     *  The order of the commits does not matter. Hint: there is a useful
     *  method in gitlet.Utils that will help you iterate over files
     *  within a directory.*/
    public static void globalLog() {
        ArrayList<Commit> allCommits = getAllCommits2();
        String output = "";
        for (Commit c : allCommits) {
            output += toStringCommit(c);
        }
        System.out.print(output);
    }


    /** Prints out the ids of all commits that have the given commit message,
     * one per line. If there are multiple such commits, it prints the ids out
     * on separate lines. The commit message is a single operand; to indicate a
     * multi-word message, put the operand in quotation marks, as for the
     * commit command above. If no such commit exists, prints the error message
     * Found no commit with that message.
     * @param args */
    public static void find(String[] args) {
        ArrayList<Commit> allCommits = getAllCommits2();
        String result = "";
        for (Commit c : allCommits) {
            if (c.getMessage().equals(args[1])) {
                result += Utils.sha1(Utils.serialize(c)) + "\n";
            }
        }
        if (result.equals("")) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
        System.out.println(result);
    }


    /** Returns an array of all commits since initial commit.
     *  A helper method. */
    public static ArrayList<Commit> getAllCommits2() {
        File[] myCommitsID = Finder.COMMIT_FOLDER.listFiles();
        ArrayList<Commit> result = new ArrayList<>();
        for (File id : myCommitsID) {
            result.add(Commit.getCommit(id.getName()));
        }
        return result;
    }


    /** Deletes the branch with the given name. This only means to
     *  delete the pointer associated with the branch; it does not
     *  mean to delete all commits that were created under the branch,
     *  or anything like that. If a branch with the given name does
     *  not exist, aborts. Print the error message A branch with that
     *  name does not exist. If you try to remove the branch you're
     *  currently on, aborts, printing the error message Cannot remove
     *  the current branch.
     *  @param args */
    public static void rmBranch(String[] args) {
        File myDirectory = Utils.join(Finder.BRANCHES_FOLDER, args[1]);
        File currentBranch = Branches.getCurrentBranch();
        if (myDirectory.equals(currentBranch)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        if (myDirectory.exists()) {
            FileUtils.cleanDirectory(myDirectory);
            myDirectory.delete();
        } else {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
    }


    /** Checks out all the files tracked by the given commit. Removes
     * tracked files that are not present in that commit. Also moves
     * the current branch's head to that commit node. See the intro
     * for an example of what happens to the head pointer after using
     * reset. The [commit id] may be abbreviated as for checkout. The
     * staging area is cleared. The command is essentially checkout of
     * an arbitrary commit that also changes the current branch head.
     * If no commit with the given id exists, print No commit with that
     * id exists. If a working file is untracked in the current branch
     * and would be overwritten by the reset, print There is an
     * untracked file in the way; delete it, or add and commit it first.
     * and exit; perform this check before doing anything else.
     * @param args */
    public static void reset(String[] args) {
        String[] args2 = new String[4];
        for (int i = 0; i < args.length; i++) {
            args2[i] = args[i];
        }
        args2[2] = "--";
        args2[3] = "reset case";
        checkout(args2);
        Commit givenCommit = Commit.getCommit(args[1]);
        Commit.clearStagingArea();
        File currentBranch = Utils.join(Finder.BRANCHES_FOLDER,
                Finder.CURRENT.listFiles()[0].getName());
        FileUtils.cleanDirectory(currentBranch);
        FileUtils.storeObject(givenCommit, currentBranch);
    }




        /** Merges files from the given branch into the current branch. If the
     *  split point is the same commit as the given branch, then we do
     *  nothing; the merge is complete, and the operation ends with the
     *  message Given branch is an ancestor of the current branch. If
     *  the split point is the current branch, then the effect is to
     *  check out the given branch, and the operation ends after
     *  printing the message Current branch fast-forwarded.
     * @param args */
    public static void merge(String[] args) {
        checkForSimpleErrors(args);
        boolean conflict = false;
        Commit currentCommit = Commit.getHeadCommit();
        Commit givenBranchCommit = Branches.getCommitFromBranch(args[1]);
        Commit splitPointCommit = getSplitPoint(givenBranchCommit,
                currentCommit);
        checkForErrors(currentCommit, givenBranchCommit, splitPointCommit,
                args[1]);
        if (givenBranchCommit.getTree() != null) {
            for (String filename
                    : givenBranchCommit.getTree().getMyFiles().keySet()) {
                if (fileChangedForMerge(filename,
                        givenBranchCommit.getTree().get(filename),
                        false, splitPointCommit)) {
                    if (!fileChangedForMerge(filename, currentCommit,
                            splitPointCommit)) {
                        match(filename, givenBranchCommit);
                    } else {
                        if (different(filename, givenBranchCommit,
                                currentCommit, false)) {
                            conflictCase(filename, givenBranchCommit,
                                    currentCommit, false);
                            conflict = true;
                        }
                    }

                }
            }
        }
        if (givenBranchCommit.getTreeRemoval() != null) {
            for (String filename
                    : givenBranchCommit.getTreeRemoval().getMyFiles()
                    .keySet()) {
                if (fileChangedForMerge(filename,
                        givenBranchCommit.getTree().get(filename), true,
                        splitPointCommit)) {
                    if (!fileChangedForMerge(filename, currentCommit,
                            splitPointCommit)) {
                        match(filename, givenBranchCommit);
                    } else {
                        if (different(filename, givenBranchCommit,
                                currentCommit, true)) {
                            conflictCase(filename, givenBranchCommit,
                                    currentCommit, true);
                            conflict = true;
                        }
                    }
                }
            }
        }
        mergeCommit("Merged " + args[1] + " into "
                + Branches.getCurrentBranch().getName()
                + ".", currentCommit);
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    private static void checkForSimpleErrors(String[] args) {
        if (args[1].equals(Branches.getCurrentBranch().getName())) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        if (!Utils.join(Finder.BRANCHES_FOLDER, args[1]).exists()) {
            System.out.println("A branch with that name does not exist");
            System.exit(0);
        }
    }

    /** This is the conflicted case for merge Here, we must
     * replace the content of the conflicted file.
     * @param filename
     * @param givenBranchCommit
     * @param currentCommit
     * @param deleted */
    private static void conflictCase(String filename,
                                     Commit givenBranchCommit,
                                     Commit currentCommit,
                                     boolean deleted) {
        if (!deleted) {
            File conflictedFile = Utils.join(Finder.CWD, filename);
            String givenID = givenBranchCommit.getTree().get(filename);
            String currentID = currentCommit.getTree().get(filename);
            File given = Blob.getFileFromBlob(givenID);
            File current = Blob.getFileFromBlob(currentID);
            Blob givenBlob = null;
            Blob currentBlob = null;
            String givenContent = "";
            String currentContent = "";
            if (given != null) {
                givenBlob = Utils.readObject(given, Blob.class);
                givenContent = givenBlob.getContentAsString();
            }
            if (current != null) {
                currentBlob = Utils.readObject(current, Blob.class);
                currentContent = currentBlob.getContentAsString();
            }
            String output = "<<<<<<< HEAD\n" + currentContent
                    + "=======\n" + givenContent + ">>>>>>>\n";
            Utils.writeContents(conflictedFile, output);
        } else {
            File conflictedFile = Utils.join(Finder.CWD, filename);
            String currentID = "";
            if (currentCommit.getTree().contains(filename)) {
                currentID = currentCommit.getTree().get(filename);
            } else if (currentCommit.getTreeRemoval().contains(filename)) {
                currentID = currentCommit.getTreeRemoval().get(filename);
            }
            File current = Blob.getFileFromBlob(currentID);
            Blob currentBlob = null;
            String givenContent = "";
            String currentContent = "";
            if (current != null) {
                currentBlob = Utils.readObject(current, Blob.class);
                currentContent = currentBlob.getContentAsString();
            }
            String output = "<<<<<<< HEAD\n" + currentContent
                    + "=======\n" + givenContent + ">>>>>>>\n";
            Utils.writeContents(conflictedFile, output);
        }
    }

    /** Returns true iff file changed differently.
     * @param filename
     * @param givenBranchCommit
     * @param currentCommit
     * @param deleted */
    private static boolean different(String filename,
                                  Commit givenBranchCommit,
                                  Commit currentCommit, boolean deleted) {
        if (deleted) {
            return !checkFileInTree(givenBranchCommit.getTreeRemoval()
                            .get(filename), currentCommit.getTreeRemoval());
        }
        return !checkFileInTree(givenBranchCommit.getTree().get(filename),
                    currentCommit.getTree());
    }


    /** Check for error cases:
     * If the split point is the same commit as the given branch,
     * then we do nothing; the merge is complete, and the operation
     * ends with the message Given branch is an ancestor of the
     * current branch. If the split point is the current branch,
     * then the effect is to check out the given branch, and the
     * operation ends after printing the message Current branch
     * fast-forwarded.
     * @param current
     * @param givenBranch
     * @param splitPoint
     * @param givenName */
    private static void checkForErrors(Commit current, Commit givenBranch,
                                       Commit splitPoint,
                                       String givenName) {
        if (Finder.ADD.length() != 0 || Finder.REMOVE.length() != 0) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        if (givenBranch.equals(splitPoint)) {
            System.out.println("Given branch is an ancestor "
                    + "of the current branch.");
            System.exit(0);
        } else if (current.equals(splitPoint)) {
            checkout(new String[] {"checkout", givenName});
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
    }

    /** Returns true if and only if fileID is in someTree.
     * This checks if files was
     * 1) modified
     * 2) deleted.
     * @param someTree
     * @param fileID */
    public static boolean checkFileInTree(String fileID, Tree someTree) {
        if (someTree.getMyFiles() == null) {
            return false;
        }
        return someTree.getMyFiles().containsValue(fileID);
    }

    /** Returns a commit which is the split point
     * between commits c1 and c2.
     * @param c1
     * @param c2 */
    private static Commit getSplitPoint(Commit c1, Commit c2) {
        if (c1.equals(c2) || c1.isInitial() || c2.isInitial()) {
            return c1;
        } else {
            Commit d1 = getSplitPoint(Commit.getCommit(c1.getParent()), c2);
            Commit d2 = getSplitPoint(c1, Commit.getCommit(c2.getParent()));
            if (d1.isInitial() && d2.isInitial()) {
                return getSplitPoint(Commit.getCommit(c1.getParent()),
                        Commit.getCommit(c2.getParent()));
            } else if (!d1.isInitial()) {
                return d1;
            } else {
                return d2;
            }
        }
    }

    /** Creates an update a mergeCommit accordingly.
     * @param message
     * @param givenBranchCommit */
    public static void mergeCommit(String message,
                                   Commit givenBranchCommit) {
        if (Finder.ADD.length() == 0 && Finder.REMOVE.length() == 0) {
            System.out.println("No changes added to the commit");
            System.exit(0);
        }
        Commit parentCommit = Commit.getHeadCommit();
        Commit newCommit = Commit.clone(parentCommit);
        Tree filesAdded = null;
        if (Finder.ADD.length() != 0) {
            filesAdded = Utils.readObject(Finder.ADD, Tree.class);
        }
        Tree filesRemove = null;
        if (Finder.REMOVE.length() != 0) {
            filesRemove = Utils.readObject(Finder.REMOVE, Tree.class);
        }
        newCommit.updateMergeCommit(message, filesAdded, filesRemove,
                Utils.sha1(Utils.serialize(parentCommit)),
                Utils.sha1(Utils.serialize(givenBranchCommit)));
        FileUtils.cleanDirectory(Branches.getCurrentBranch());
        FileUtils.storeObject(newCommit, Branches.getCurrentBranch());
        FileUtils.storeObject(newCommit, Finder.COMMIT_FOLDER);
    }



    /** A helper method for merge. Takes in 4 parameters:
     * @param filename
     * @param fileID
     * @param deleted
     * @param c
     * Returns true if and only if filename with fileID was changed
     * according to commit c. Notices that if deleted is true that means
     * that file was deleted. */
    public static boolean fileChangedForMerge(String filename, String fileID,
                                              boolean deleted, Commit c) {
        if (c.getTree() == null && c.getTreeRemoval() == null) {
            return true;
        }
        if (deleted && c.getTree().contains(filename)) {
            return true;
        }
        if (deleted && !c.getTreeRemoval().contains(filename)) {
            return true;
        }
        if (!deleted && !c.getTree().contains(filename)) {
            return true;
        }
        if (!deleted && c.getTree().contains(filename)) {
            return !c.getTree().get(filename).equals(fileID);
        }
        return false;
    }

    /** A helper method for merge. Takes in 3 parameters:
     * @param filename
     * @param from
     * @param to
     * Returns true if and only if filename with fileID was changed.
     * Such file came from commit FROM, and we are comparing to
     * commit to. */
    public static boolean fileChangedForMerge(String filename, Commit from,
                                              Commit to) {
        if (from.getTree().contains(filename)) {
            return fileChangedForMerge(filename,
                    from.getTree().get(filename), false, to);
        }
        if (from.getTreeRemoval().contains(filename)) {
            return fileChangedForMerge(filename,
                    from.getTree().get(filename), true, to);
        }
        if (to.getTree() == null || to.getTreeRemoval() == null) {
            return false;
        }
        return to.getTree().contains(filename)
                || to.getTreeRemoval().contains(filename);
    }




    /** Matches filename form commit c.
     * @param c
     * @param filename */
    public static void match(String filename, Commit c) {
        Tree treeADD = c.getTree();
        Tree treeRemove = c.getTreeRemoval();
        Commit current = Commit.getHeadCommit();
        if (treeADD.contains(filename)) {
            if (willBeOverwritten(filename, treeADD)
                    && !current.getTree().contains(filename)) {
                System.out.println("There is an untracked "
                        + "file in the way; delete it, "
                        + "or add and commit it first.");
                System.exit(0);
            }
            recoverBlob(treeADD, filename);
            stageFile(filename, treeADD.get(filename), false);
        } else if (treeRemove.contains(filename)) {
            File thisFile = Utils.join(Finder.CWD, filename);
            if (thisFile.exists()) {
                thisFile.delete();
                stageFile(filename, treeRemove.get(filename), true);
            }
        }
    }


    /** Stage file (add file to staging area). If forDeletion
     * is true, stage file for removal, otherwise stage it for addition.
     * @param fileID
     * @param filename
     * @param forDeletion */
    public static void stageFile(String filename, String fileID,
                                 boolean forDeletion) {
        if (!forDeletion) {
            Tree forAddition;
            if (Finder.ADD.length() != 0) {
                forAddition = Utils.readObject(Finder.ADD, Tree.class);
            } else {
                forAddition = new Tree();
            }
            forAddition.getMyFiles().put(filename, fileID);
            Utils.writeObject(Finder.ADD, forAddition);
        } else {
            Tree forDel;
            if (Finder.REMOVE.length() != 0) {
                forDel = Utils.readObject(Finder.REMOVE, Tree.class);
            } else {
                forDel = new Tree();
            }
            forDel.getMyFiles().put(filename, fileID);
            Utils.writeObject(Finder.REMOVE, forDel);
        }
    }




}
