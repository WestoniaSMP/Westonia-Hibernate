package coffee.j4n.westonia.utils;

import java.io.File;

/**
 * This class provides static methods to handle files.
 */
public class FileUtils {

    /**
     * Creates the parent directories of the given file if they do not exist.
     *
     * @param file The file to create the parent directories for.
     */
    public static void createParentDirectories(File file) {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }
}