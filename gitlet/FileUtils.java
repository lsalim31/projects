package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/** This is a class responsible for methods dealing with Files.
 * @author Lucas Salim. */
public class FileUtils {

    /** Creates the file for a given FILE.
     * @param file */
    public static void createFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Creates the directory for a given FILE (that represents
     * some directory). No error if already exists.
     * @param files */
    public static void createFiles(File... files) {
        for (File f: files) {
            createFile(f);
        }
    }

    /** Creates the directory for a given FILE (that represents
     * some directory). Throw GitletException for a given MESSAGE
     * if FILE already exists.
     * @param file
     * @param message */
    public static void createDirectory(File file, String message) {
        if (!file.exists()) {
            file.mkdir();
        } else {
            System.out.println(message);
            System.exit(0);
        }
    }

    /** Creates the directory for a given FILE (that represents
     * some directory). No error if already exists.
     * @param file */
    public static void createDirectory(File file) {
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /** Creates the directory for a given FILE (that represents
     * some directory). No error if already exists.
     * @param files */
    public static void createDirectories(File... files) {
        for (File f: files) {
            createDirectory(f);
        }
    }

    /** Erase all files on directory in FILEPATH.
     * @param filepath */
    public static void cleanDirectory(File filepath) {
        for (File f : filepath.listFiles()) {
            f.delete();
        }
    }


    /** Clear a directory recursively.
     * @param filepath */
    public static void deleteDir(File filepath) {
        if (filepath.isFile() || filepath.length() == 0) {
            filepath.delete();
        } else {
            for (File f : filepath.listFiles()) {
                deleteDir(f);
            }
        }
    }

    /** Store OBJ in DIRECTORY, with name of file being the content ID.
     * @param directory
     * @param obj */
    public static void storeObject(Serializable obj, File directory) {
        String name = Utils.sha1(Utils.serialize(obj));
        File myFile = Utils.join(directory, name);
        createFile(myFile);
        Utils.writeObject(myFile, obj);
    }

    /** Check if all files in files exists, if one of them don't,
     * print message.
     * @param files
     * @param message */
    public static void checkExistence(String[] files, String message) {
        for (int i = 1; i < files.length; i++) {
            File file = Utils.join(Finder.CWD, files[i]);
            if (!file.exists()) {
                System.out.println(message);
                System.exit(0);
            }
        }
    }


}
