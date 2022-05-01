package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

/** Connects a filename to its content Blob.
 * @author Lucas Salim. */
public class Tree implements Serializable {

    /** Data structure for my files. */
    private HashMap<String, String> myFiles;

    public HashMap<String, String> getMyFiles() {
        return myFiles;
    }

    public Tree() {
        myFiles = new HashMap<>();
    }

    /** Add file to this Tree.
     * @param file */
    public void add(File file) {
        Blob myBlob = new Blob(file);
        String blobName = Utils.sha1(Utils.serialize(myBlob));
        FileUtils.storeObject(myBlob, Finder.BLOBS_FOLDER);
        myFiles.put(file.getName(), blobName);
    }

    /** @return true if and only if filename in this.
     * @param filename */
    public boolean contains(String filename) {
        return myFiles.containsKey(filename);
    }

    /** @return object for given key filename.
     * @param filename */
    public String get(String filename) {
        return myFiles.get(filename);
    }

    /** store new Tree on top of this Tree.
     * @param tree */
    public void putAll(Tree tree) {
        this.myFiles.putAll(tree.myFiles);
    }


    /** This compares two Trees. The first argument is the Tree in the
     * current Staging Area. The second argument is the Tree in the
     * head commit. This method returns a Tree that does not have
     * common blobs (like set). t1 is the dominant file. Namely,
     * this returns t1 - t2, in a sense.
     * @param t1
     * @param t2 */
    public static Tree compareTrees(Tree t1, Tree t2) {
        if (t1 == null) {
            return t2;
        }
        if (t2 == null) {
            return t1;
        }
        Tree t = new Tree();
        for (String name1 : t1.myFiles.keySet()) {
            if (!t2.myFiles.containsKey(name1)) {
                t.myFiles.put(name1, t1.myFiles.get(name1));
            } else {
                String blob1Name = t1.myFiles.get(name1);
                String blob2Name = t2.myFiles.get(name1);
                if (!blob1Name.equals(blob2Name)) {
                    t.myFiles.put(name1, t1.myFiles.get(name1));
                }
            }
        }
        return t;
    }

    /** Remove name from this.
     * @param name */
    public void remove(String name) {
        myFiles.remove(name);
    }

}
