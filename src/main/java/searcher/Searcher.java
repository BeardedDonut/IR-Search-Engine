package searcher;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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


    private Searcher(String indexDirectoryPath, CharArraySet stopWords, String indexDirectory, String indexContentTag)
            throws IOException {
        this.indexDirectory = indexDirectory;
        this.indexContentTag = indexContentTag;

        Directory indexDir = FSDirectory.open(Paths.get(indexDirectory));
        IndexReader indexReader = DirectoryReader.open(indexDir);

        indexSearcher = new IndexSearcher(indexReader);

        queryParser = new QueryParser(indexContentTag, new EnglishAnalyzer(stopWords));
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
    public int getTermFrequency(String term, int docId) throws IOException {
        Directory indexDir = FSDirectory.open(Paths.get(this.indexDirectory));
        IndexReader indexReader = DirectoryReader.open(indexDir);

        Terms vector = indexReader.getTermVector(docId, this.indexContentTag);
        TermsEnum termsEnum = vector.iterator();
        Map<String, Integer> frequencies = new HashMap<String, Integer>();
        BytesRef text = null;

        try {
            term = generateQuery(term).toString(this.indexContentTag);

            while ((text = termsEnum.next()) != null) {
                String tempTerm = text.utf8ToString();
                if (tempTerm.equals(term)) {
                    return (int) termsEnum.totalTermFreq();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
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
    public int gerDocumentFrequency(String term) {
        return 0;
    }
}
