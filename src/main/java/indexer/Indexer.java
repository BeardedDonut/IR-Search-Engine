package indexer;

import fileFilter.TextFileFilter;
import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author navid
 *         Project-Name: IR-Search-Engine
 *         Date: 5/29/18.
 */
public class Indexer implements IndexerInterface{

    public static final String SUMMARY_NAME = "summary-file-name";
    public static final String SUMMARY_CONTENT = "summary-content";

    private IndexWriter writer;

    public Indexer(String indexDirectoryPath, Analyzer analyzer) throws IOException {
        // Directory to store indexes
        File f = new File(indexDirectoryPath);
        Directory indexDirectory = FSDirectory.open(f.toPath());

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        writer = new IndexWriter(indexDirectory, config);
    }

    public void close() throws CorruptIndexException, IOException {
        writer.close();
    }

    /**
     * This method takes a file handle and a file name and creates a document which its name
     * field will be file name and the content is the file handle
     * @param file: given file
     * @param fileName: given file name
     * @return: Document generated document
     * @throws IOException
     */
    private Document analyzeDocument(File file, String fileName) throws IOException {
        Document doc = new Document();

        // content field
        // Field contentField = new TextField(ProjectConstants.SUMMARY_CONTENT, new FileReader(file));

        FieldType fieldType = new FieldType();
        fieldType.setStoreTermVectors(true);
        fieldType.setTokenized(true);
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);

        Field newContent = new Field(SUMMARY_CONTENT, new FileReader(file), fieldType);

        // number field
        Field nameField = new StringField(SUMMARY_NAME, fileName, Field.Store.YES);

        // adding fields
        doc.add(newContent);
        doc.add(nameField);

        // return the document
        return doc;
    }

    /**
     * this method takes a summary file and indexes it
     * @param summary: summary file
     * @param fileName: summary file name
     * @throws IOException
     */
    private void indexSummary(File summary, String fileName) throws IOException {
        System.out.println("Indexing summary: '" + summary.getName() + "'");

        Document summaryDoc = analyzeDocument(summary, fileName);
        writer.addDocument(summaryDoc);
    }

    /**
     * This method iterates all the summary files in the given path and indexes each one of them.
     *
     * @param decomposedSummariesPath: decomposed summary file
     * @param filter: a file filter ... such as accepting only `.txt` files
     * @return: number of docs indexed
     * @throws IOException
     */
    public int createIndex(String decomposedSummariesPath, FileFilter filter) throws IOException {

        // get all the summaries in the given path
        File[] files = new File(decomposedSummariesPath).listFiles();

        for (File file: files) {

            // check the conditions
            boolean condition = !file.isDirectory()
                    && !file.isHidden()
                    && file.exists()
                    && file.canRead()
                    && filter.accept(file);

            if (condition) {
                int summaryNumber = Integer.parseInt(FilenameUtils.removeExtension(file.getName()));
                String name = file.getName();
                indexSummary(file, name);
            }

        }

        return writer.numDocs();
    }

    public static void main(String[] args) { // TODO: remove the code below its just for test
        try {
            Indexer test = new Indexer("./indexes/", new EnglishAnalyzer());
            test.createIndex("./decomposedCorpus/", new TextFileFilter());
            test.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
