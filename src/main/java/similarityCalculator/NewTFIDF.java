package similarityCalculator;

/**
 * @author navid
 *         Project-Name: IR-Search-Engine
 *         Date: 5/31/18.
 *
 * New similarity measurment strategy which uses:
 *      - squareroot of termfrequency as the TF measurement
 *      - "prob idf" for DF
 */
public class NewTFIDF extends BaseTFIDFScoringStrategy {

    @Override
    public float tf(float freq) {
        return (float)Math.sqrt((double)freq);
    }

    @Override
    public float idf(long docFreq, long docCount) {
        double a = ((double)(docCount) - (double)(docFreq));
        double b = (double)(docFreq);

        return (float)(Math.max(0, Math.log(a/b)));
    }
}
