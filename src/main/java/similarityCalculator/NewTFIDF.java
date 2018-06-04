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
        return (float)( 1.0D + Math.log(freq));
    }

    @Override
    public float idf(long docFreq, long docCount) {
        System.out.println(docFreq + ", " + docCount);
        return (float)(Math.log((double)(docCount) / (double)(docFreq)));
    }
}
