import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import corpusDecomposer.TCCorpusDecomposer;
import fileFilter.TextFileFilter;
import indexer.Indexer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.IOException;
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

    private static ColoredPrinter cp = new ColoredPrinter.Builder(1, false)
            .build();

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

    private static void createIndexes(String pathToDocuments, String pathToSaveIndexes) {
        boolean cond = pathToDocuments != null
                && pathToSaveIndexes != null
                && !pathToDocuments.toLowerCase().trim().equals("")
                && !pathToSaveIndexes.toLowerCase().trim().equals("");

        if (!cond) {
            return;
        }

        try {
            Indexer idx = new Indexer(pathToSaveIndexes, new StandardAnalyzer());
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

    private static boolean execute(String command) {
        String[] parsedString = command.split(" ");
        String baseCommand = parsedString[0].toLowerCase();

        if (baseCommand.equals("indexer")) {
            createIndexes(parsedString[1], parsedString[2]);
        } else if (baseCommand.equals("decompose")) {
            decomposeCorpusFile(parsedString[1], parsedString[2]);
        } else if (baseCommand.equals("query_id")) {
            // TODO: implement query with query_id
        } else if (baseCommand.equals("query")) {
            // TODO: implement query
        } else if (baseCommand.equals("help")) {
            // TODO: implement help
        } else if (baseCommand.equals("quit")) {
            return false;
        }

        return true;
    }

    public static void init() {
        printInfo("Small Search Engine Application- by Navid Alipour");
        Scanner sc = new Scanner(System.in);
        boolean isApplicationOn = true;

        while (isApplicationOn) {
            printInfo("Please Enter Your Command (enter 'help' for more info)");
            String line = sc.nextLine();
            isApplicationOn = execute(line);
        }
    }

    public static void main(String[] args) {
        init();
    }
}
