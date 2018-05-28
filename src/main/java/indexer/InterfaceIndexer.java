package indexer;

import java.io.FileFilter;

/**
 * @author navid
 *         Project-Name: IR-Search-Engine
 *         Date: 5/28/18.
 */
public interface InterfaceIndexer {
    void createIndex(String pathToCorpus, FileFilter fileFilter);
}
