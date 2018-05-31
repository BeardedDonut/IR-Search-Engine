package similarityCalculator;

import org.apache.lucene.search.similarities.ClassicSimilarity;

/**
 * @author navid
 *         Project-Name: IR-Search-Engine
 *         Date: 5/31/18.
 */
public class NewTFIDF extends ClassicSimilarity {

    @Override
    public float tf(float freq) {
        return (float)Math.sqrt((double)freq);
    }

    @Override
    public float idf(long docFreq, long docCount) {
        return (float)(Math.log((double)(docCount + 1L) / (double)(docFreq + 1L)) + 1.0D);
    }
}
