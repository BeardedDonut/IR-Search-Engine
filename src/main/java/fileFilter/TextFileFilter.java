package fileFilter;

import java.io.File;
import java.io.FileFilter;

/**
 * @author navid
 *         Project-Name: IR-Search-Engine
 *         Date: 5/28/18.
 */
public class TextFileFilter implements FileFilter {
    public boolean accept(File file) {
        return file.getName().toLowerCase().endsWith(".txt");
    }
}
