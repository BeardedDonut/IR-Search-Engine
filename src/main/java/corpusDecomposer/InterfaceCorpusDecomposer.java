package corpusDecomposer;

import java.io.FileFilter;

/**
 * @author navid
 *         Project-Name: IR-Search-Engine
 *         Date: 5/28/18.
 */
public interface InterfaceCorpusDecomposer {
    void decompose(String pathToCorpusFile, FileFilter fileFilter, String pathToSaveResult);
}
