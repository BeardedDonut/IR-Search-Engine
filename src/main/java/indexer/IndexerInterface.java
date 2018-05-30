package indexer;

import java.io.FileFilter;
import java.io.IOException;

/**
 * @author navid
 *         Project-Name: IR-Search-Engine
 *         Date: 5/28/18.
 */
public interface IndexerInterface {
    int createIndex(String pathToCorpus, FileFilter fileFilter) throws IOException;
}
