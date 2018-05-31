/**
 * @author navid
 *         Project-Name: IR-Search-Engine
 *         Date: 5/31/18.
 */
public class QueryResult {
    String documentName;
    String query;

    float simScore;
    int rank;

    public QueryResult(String documentName, String query, float simScore, int rank) {
        this.documentName = documentName;
        this.query = query;
        this.simScore = simScore;
        this.rank = rank;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public float getSimScore() {
        return simScore;
    }

    public void setSimScore(float simScore) {
        this.simScore = simScore;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
