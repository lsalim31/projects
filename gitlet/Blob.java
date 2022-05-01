package gitlet;

import java.io.File;
import java.io.Serializable;

/** A serializable class to represent the content of a file.
 * @author Lucas Salim */
public class Blob implements Serializable {

    /** A variable to store the content of our Blob. */
    private byte[] _content;

    /** A variable to store the content of our Blob as a string. */
    private String _contentAsString;

    public Blob(File file) {
        _content = Utils.readContents(file);
        _contentAsString = Utils.readContentsAsString(file);
    }

    /** Returns the content of this blob. */
    public byte[] getContent() {
        return _content;
    }

    /** Returns the content of this blob as string.*/
    public String getContentAsString() {
        return _contentAsString;
    }

    /** Returns the file for the given blobID.
     * @param blobID */
    public static File getFileFromBlob(String blobID) {
        File thisFile = Utils.join(Finder.BLOBS_FOLDER, blobID);
        if (thisFile.exists()) {
            return thisFile;
        } else {
            return null;
        }
    }
}
