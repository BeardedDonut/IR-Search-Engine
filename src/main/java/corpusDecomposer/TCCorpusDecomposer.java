package corpusDecomposer;

import java.io.*;

/**
 * @author navid
 *         Project-Name: IR-Search-Engine
 *         Date: 5/28/18.
 */
public class TCCorpusDecomposer {


    public static void startDecomposition(String pathToCorpusFile, String pathToSaveResult) {
        try {
            // create file reader
            FileReader fileReader = new FileReader(pathToCorpusFile);

            // create buffered reader from the file reader
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            decomposeNext(pathToCorpusFile, bufferedReader, pathToSaveResult, 1);

            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + pathToCorpusFile + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + pathToCorpusFile + "'");
        }
    }

    private static void decomposeNext(String pathToCorpusFile, BufferedReader bfr, String pathToSaveResult, int docNumber)
            throws IOException {

        String line = bfr.readLine();

        if ((line) == null) {
            return;
        }

        while (line != null && docNumber < 10) { // TODO: remove the doc limit its just for test

            if (line.charAt(0) == '#') {
                PrintWriter writer = new PrintWriter(pathToSaveResult + "/" + docNumber + ".txt", "UTF-8");
                line = bfr.readLine();

                // getting the summary and writing it in a separate file.
                while (line != null && line.charAt(0) != '#') {
                    writer.write(line);
                    line = bfr.readLine();
                }

                writer.close();
                docNumber++;
            }
        }
    }
}
