package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Commit implements Serializable {

    /** A place to store the shortUID value. */
    private static final int SHORTUID = 8;

    /** A place to store the commit message. */
    private String _message;

    /** A place to store the add Tree. */
    private Tree _TreeADD;

    /** A place to store the remove Tree. */
    private Tree _TreeREMOVE;

    /** A place to store the parent commit. */
    private String _parent;

    /** A place to store the second parent commit. */
    private String _parent2;

    /** A place to store the date of the commit. */
    private Date _date;

    /** Our initial year. */
    private static final int YEAR = 1970;

    public Commit() {
        _TreeADD = null;
        _TreeREMOVE = null;
        _parent = null;
        String pattern = "EEE MMM d HH:mm:ss yyyy Z";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        GregorianCalendar lol = new GregorianCalendar(YEAR,
                Calendar.JANUARY, 1);
        lol.setTimeInMillis(0);
        _date = lol.getTime();
        _message = "initial commit";
    }

    public Commit(String message, Tree treeADD, Tree treeREMOVE, Date date) {
        _message = message;
        _TreeADD = treeADD;
        _TreeREMOVE = treeREMOVE;
        _date = date;
    }

    public static Commit clone(Commit c) {
        Commit result = new Commit(c.getMessage(), c.getTree(),
                c.getTreeRemoval(), c._date);
        return result;
    }

    public static Commit getInitialCommit() {
        Commit prevCommit = Commit.getHeadCommit();
        while (!prevCommit.isInitial()) {
            prevCommit = Commit.getCommit(prevCommit.getParent());
        }
        return prevCommit;
    }

    public static void clearRemovalArea() {
        Finder.REMOVE.delete();
        FileUtils.createFile(Finder.REMOVE);
    }

    public void updateCommit(String message, Tree tree, Tree treeRemove,
                             String parent) {
        _message = message;
        _parent = parent;
        if (_TreeADD == null) {
            _TreeADD = tree;
        } else {
            if (!(tree == null)) {
                _TreeADD.putAll(tree);
            }
        }
        _TreeADD = Tree.compareTrees(_TreeADD, treeRemove);
        if (_TreeREMOVE == null) {
            _TreeREMOVE = treeRemove;
        } else {
            if (!(treeRemove == null)) {
                _TreeREMOVE.putAll(treeRemove);
            }
        }
        _date = new Date();
        clearStagingArea();
    }

    public void updateMergeCommit(String message, Tree tree, Tree treeRemove,
                                  String parent, String parent2) {
        updateCommit(message, tree, treeRemove, parent);
        _parent2 = parent;
    }

    public static void clearStagingArea() {
        Finder.ADD.delete();
        FileUtils.createFile(Finder.ADD);
        Finder.REMOVE.delete();
        FileUtils.createFile(Finder.REMOVE);
    }

    public static Commit getCommit(String name) {
        File commitFile = Utils.join(Finder.COMMIT_FOLDER, name);
        File[] allCommits = Finder.COMMIT_FOLDER.listFiles();
        if (allCommits != null) {
            for (File f : allCommits) {
                if (f.getName().substring(0, SHORTUID).equals(name)) {
                    return Utils.readObject(f, Commit.class);
                }
            }
        }
        if (!commitFile.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        return Utils.readObject(commitFile, Commit.class);
    }

    public String getParent() {
        return _parent;
    }

    public String getMessage() {
        return _message;
    }

    public Tree getTree() {
        return _TreeADD;
    }

    public Tree getTreeRemoval() {
        return _TreeREMOVE;
    }

    public boolean isInitial() {
        return ((_TreeADD == null) && (_parent == null));
    }

    public static Commit getHeadCommit() {
        return Branches.getCurrentBranchCommit();
    }

    public Date getDate() {
        return _date;
    }
}
