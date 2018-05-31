package searcher;

import indexer.Indexer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import similarityCalculator.NewTFIDF;

import javax.print.Doc;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author navid
 *         Project-Name: IR-Search-Engine
 *         Date: 5/31/18.
 */
public class Searcher implements SearcherInterface {
    private IndexSearcher indexSearcher;
    private QueryParser queryParser;
    private String indexDirectory;
    private String indexContentTag;
    private TFIDFSimilarity mySimilarity;

    public Searcher(CharArraySet stopWords, String indexDirectory, String indexContentTag, TFIDFSimilarity newSimilarity)
            throws IOException {
        this.indexDirectory = indexDirectory;
        this.indexContentTag = indexContentTag;
        this.mySimilarity = newSimilarity;

        Directory indexDir = FSDirectory.open(Paths.get(indexDirectory));
        IndexReader indexReader = DirectoryReader.open(indexDir);

        indexSearcher = new IndexSearcher(indexReader);
        indexSearcher.setSimilarity(this.mySimilarity);

        queryParser = new QueryParser(indexContentTag, new EnglishAnalyzer(stopWords));
    }

    public Searcher(CharArraySet stopWords, String indexDirectory, String indexContentTag) throws IOException {
        this(stopWords, indexDirectory, indexContentTag, new ClassicSimilarity());
    }

    @Override
    public TopDocs search(String queryString, int limit) {

        try {
            Query query = this.generateQuery(queryString);
            return this.indexSearcher.search(query, 10);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public TopDocs search(Query query, int limit) {
        try {
            return this.indexSearcher.search(query, limit);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Query generateQuery(String queryString) throws ParseException {
        return queryParser.parse(queryString);
    }

    @Override
    public Document getDocument(ScoreDoc scoreDoc) throws IOException {
        return indexSearcher.doc(scoreDoc.doc);
    }

    @Override
    public void setSimilarity(TFIDFSimilarity newSimilarity) {
        this.mySimilarity = newSimilarity;
    }

    public static void main(String[] args) throws IOException { // TODO: remove the code below its just for test
        Searcher mySearcher = new Searcher(CharArraySet.EMPTY_SET, "./indexes/","summary-content", new NewTFIDF());
        TopDocs x = mySearcher.search("program comput procedur ground terminolog engin", 100);
        System.out.println(x.totalHits);

        if (x.totalHits > 0) {
            for(ScoreDoc scoreDoc: x.scoreDocs) {
                Document d = mySearcher.getDocument(scoreDoc);
                System.out.println("scoreDoc: doc-id:" + d.getField(Indexer.SUMMARY_NAME).stringValue() + ", score: " + scoreDoc.score);
            }
        }
    }
}
