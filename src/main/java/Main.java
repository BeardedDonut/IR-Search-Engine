import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import corpusDecomposer.TCCorpusDecomposer;
import fileFilter.TextFileFilter;
import indexer.Indexer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import searcher.Searcher;
import similarityCalculator.NewTFIDF;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author navid
 *         Project-Name: IR-Search-Engine
 *         Date: 5/28/18.
 */
public class Main {
    public static final String DECOMPOSED_SUMMARY_DIRECTORY = "./decomposedSummaries/";
    public static final String INDEX_DIRECTORY = "./indexes/";
    public static final String SOURCE_SUMMARY_FILE_PATH = "./resources/tccorpus.txt";

    private static ColoredPrinter cp = new ColoredPrinter.Builder(1, false).build();

    private static Searcher mySearcher;
    private static Indexer idx;

    public static void main(String[] args) {
        init();
    }

    private static void init() {
        printInfo("Small Search Engine Application- by Navid Alipour");
        Scanner sc = new Scanner(System.in);
        boolean isApplicationOn = true;

        while (isApplicationOn) {
            printInfo("Please Enter Your Command (enter 'help' for more info)");
            String line = sc.nextLine();
            isApplicationOn = execute(line);
        }
    }

    private static boolean execute(String command) {
        String[] parsedString = command.split(" ");
        String baseCommand = parsedString[0].toLowerCase();

        if (baseCommand.equals("indexer")) {
            createIndexes(parsedString[1], parsedString[2]);
        } else if (baseCommand.equals("decompose")) {
            decomposeCorpusFile(parsedString[1], parsedString[2]);
        } else if (baseCommand.equals("newtfidf")) {
            printQueryResultList(search(parsedString[1], parsedString[2], Integer.parseInt(parsedString[3])));
        } else if (baseCommand.equals("help")) {
            printHelp();
        } else if (baseCommand.equals("quit")) {
            return false;
        }

        return true;
    }

    private static void createIndexes(String pathToDocuments, String pathToSaveIndexes) {
        boolean cond = pathToDocuments != null
                && pathToSaveIndexes != null
                && !pathToDocuments.toLowerCase().trim().equals("")
                && !pathToSaveIndexes.toLowerCase().trim().equals("");

        if (!cond) {
            return;
        }

        try {
            idx = new Indexer(pathToSaveIndexes, new StandardAnalyzer());
            idx.createIndex(pathToDocuments, new TextFileFilter());
            idx.close();

            printSuccess("Indexes are created at [" + pathToSaveIndexes + "] successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void decomposeCorpusFile(String pathToCorpusFile, String pathToSaveDecompositionRes) {
        boolean cond = pathToCorpusFile != null
                && pathToSaveDecompositionRes != null
                && !pathToCorpusFile.toLowerCase().trim().equals("")
                && !pathToSaveDecompositionRes.toLowerCase().trim().equals("");

        if (!cond) {
            return;
        }

        int n = TCCorpusDecomposer.startDecomposition(pathToCorpusFile, pathToSaveDecompositionRes);

        if (n == -1) {
            printError("Oops something went wrong!");
        }

        printSuccess(n + " documents are created at [" + pathToSaveDecompositionRes + "] successfully.");
    }

    public static ArrayList<QueryResult> search(String pathToIndexFiles, String pathToQueryFile, int limit) {

        ArrayList<QueryResult> qResList = new ArrayList<>();

        boolean cond = pathToIndexFiles != null
                && pathToQueryFile != null
                && limit > 0
                && !pathToIndexFiles.toLowerCase().trim().equals("")
                && !pathToQueryFile.toLowerCase().trim().equals("");

        if(cond) {
            try {
                mySearcher = new Searcher(CharArraySet.EMPTY_SET, pathToIndexFiles, Indexer.SUMMARY_CONTENT, new NewTFIDF());

                FileReader fileReader = new FileReader(pathToQueryFile);
                BufferedReader bfr = new BufferedReader(fileReader);
                String queryString = bfr.readLine();
                int queryId = 1;


                while (queryString != null) {
                    TopDocs tpDocs = mySearcher.search(queryString, limit);

                    if(tpDocs.totalHits == 0) {
                        queryString = bfr.readLine();
                        queryId++;

                        continue;
                    }
                    int rank = 1;

                    for(ScoreDoc scoreDoc: tpDocs.scoreDocs) {
                        Document doc = mySearcher.getDocument(scoreDoc);
                        String docName = doc.getField(Indexer.SUMMARY_NAME).stringValue();

                        if (scoreDoc.score == 0.0000f) {
                            continue;
                        }

                        QueryResult newQRes = new QueryResult(docName, queryString, scoreDoc.score, rank, queryId);
                        qResList.add(newQRes);

                        rank++;
                    }

                    queryString = bfr.readLine();
                    queryId++;
                }

            } catch (IOException e) {
                printError("Oops Something went wrong!");
                e.printStackTrace();
            }
        }

        return qResList;
    }

    private static void printQueryResultList(ArrayList<QueryResult> queryResults) {
        if (queryResults.size() == 0) {
            printError("No Query Results!");
            return;
        }

        for(QueryResult qr: queryResults) {
            StringBuilder x = new StringBuilder();
            x.append("query_id: ").append(qr.getQueryId()).append("\t");
            x.append("doc_id: ").append(qr.getDocumentName()).append("\t");
            x.append("rank: ").append(qr.getRank()).append("\t");
            x.append("score: ").append(qr.getSimScore()).append("\t");

            printInfo(x.toString());
        }
    }

    private static void printHelp() {
        StringBuilder x = new StringBuilder();

        x.append("Creating Indexes:\n\tindexer <path-to-documents> <path-to-save-index-results> \n");
        x.append("Example:\n\tindexer ./decomposedCorpus/ ./indexes/\n\n");

        x.append("Decompose Corpus: \n\tdecompose <path-to-corpus-file> <path-to-save-decomposition-result>\n");
        x.append("Example: \n\tdecompose ./resources/tccorpus.txt ./decomposedCorpus/\n\n");

        x.append("Run TF-IDF algorithm: \n\tnewtfidf <path-to-index-files> <path-to-query-file> <limit>\n");
        x.append("Example: \n\tnewtfidf ./inedexes/ ./resources/queries.txt 100\n\n");

        x.append("Terminate The Program: \n\tquit\n");

        printSuccess(x.toString());
    }



    private static void printError(String msg) {
        cp.print(msg + "\n", Ansi.Attribute.BOLD, Ansi.FColor.RED, Ansi.BColor.NONE);
        cp.clear();
    }

    private static void printSuccess(String msg) {
        cp.print(msg + "\n", Ansi.Attribute.BOLD, Ansi.FColor.GREEN, Ansi.BColor.NONE);
        cp.clear();
    }

    private static void printInfo(String msg) {
        cp.print(msg + "\n", Ansi.Attribute.BOLD, Ansi.FColor.CYAN, Ansi.BColor.NONE);
        cp.clear();
    }
}
