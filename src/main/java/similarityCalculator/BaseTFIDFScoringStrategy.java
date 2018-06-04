package similarityCalculator;

import org.apache.lucene.search.similarities.ClassicSimilarity;

/**
 * @author navid
 *         Project-Name: IR-Search-Engine
 *         Date: 6/5/18.
 */
public abstract class BaseTFIDFScoringStrategy extends ClassicSimilarity {
    public abstract float tf(float freq);
    public abstract float idf(long docFreq, long docCount);
}
