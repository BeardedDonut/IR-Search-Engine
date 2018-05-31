package searcher;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.TFIDFSimilarity;

import java.io.IOException;

/**
 * @author navid
 *         Project-Name: IR-Search-Engine
 *         Date: 5/28/18.
 */
public interface SearcherInterface {
    // TODO: define a proper interface
    TopDocs search(String queryString, int limit);

    TopDocs search(Query query, int limit);

    Query generateQuery(String queryString) throws ParseException;

    Document getDocument(ScoreDoc scoreDoc) throws IOException;

    void setSimilarity(TFIDFSimilarity newSimilarity);
}
