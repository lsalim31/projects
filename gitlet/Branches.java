package gitlet;

import java.io.File;


/** A class to deal with Branches in gitlet.
 * @author Lucas Salim. */
public class Branches {


    /** Returns directory that contains the current branch.
     * Namely, it originally returns MASTER. */
    public static File getCurrentBranch() {
        String name = Finder.CURRENT.listFiles()[0].getName();
        return Utils.join(Finder.BRANCHES_FOLDER, name);
    }

    /** Returns the last commit from current branch. */
    public static Commit getCurrentBranchCommit() {
        File myBranch = getCurrentBranch();
        File myHead = myBranch.listFiles()[0];
        return Utils.readObject(myHead, Commit.class);
    }

    /** Returns head commit from given branch branchName.
     * @param branchName */
    public static Commit getCommitFromBranch(String branchName) {
        File myBranch = Utils.join(Finder.BRANCHES_FOLDER, branchName);
        if (!myBranch.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        File myHead = myBranch.listFiles()[0];
        return Utils.readObject(myHead, Commit.class);
    }

    /** Set current branch to BRANCH_NAME.
     * @param branchName */
    public static void updateBranch(String branchName) {
        if (getCurrentBranch().getName().equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        FileUtils.cleanDirectory(Finder.CURRENT);
        File newCurrent = Utils.join(Finder.CURRENT, branchName);
        FileUtils.createFile(newCurrent);
    }
}

