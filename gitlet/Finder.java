package gitlet;

import java.io.File;

/** A class that stores all my Directories and Files.
 * @author Lucas Salim. */
public class Finder {

    /** My CWD. */
    public static final File CWD = new File(".");

    /** My CWD. */
    public static final File GITLET_FOLDER = Utils.join(CWD, ".gitlet");

    /** My CWD. */
    public static final File COMMIT_FOLDER = Utils.join(GITLET_FOLDER,
            "COMMITS");

    /** My CWD. */
    public static final File BRANCHES_FOLDER = Utils.join(GITLET_FOLDER,
            "BRANCHES");

    /** My CWD. */
    public static final File CURRENT = Utils.join(BRANCHES_FOLDER, "CURRENT");

    /** My CWD. */
    public static final File MASTER = Utils.join(BRANCHES_FOLDER, "master");

    /** My CWD. */
    public static final File MASTER_POINTER = Utils.join(CURRENT, "master");

    /** My CWD. */
    public static final File STAGING_AREA = Utils.join(GITLET_FOLDER,
            "STAGING");

    /** My CWD. */
    public static final File ADD = Utils.join(STAGING_AREA, "ADD");

    /** My CWD. */
    public static final File REMOVE = Utils.join(STAGING_AREA, "REMOVE");

    /** My CWD. */
    public static final File BLOBS_FOLDER = Utils.join(GITLET_FOLDER,
            "BLOBS");

    /** My CWD. */
    public static final File[] DIRECTORIES =
        {COMMIT_FOLDER, BRANCHES_FOLDER,
        MASTER, STAGING_AREA, BLOBS_FOLDER, CURRENT, MASTER_POINTER};

    /** My CWD. */
    public static final File[] FILES = {ADD, REMOVE};
}
