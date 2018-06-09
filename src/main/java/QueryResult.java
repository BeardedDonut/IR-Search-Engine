/**
 * @author navid
 *         Project-Name: IR-Search-Engine
 *         Date: 5/31/18.
 */
public class QueryResult {
    private String documentName;
    private String query;

    private float simScore;
    private int rank;
    private int queryId;

    public QueryResult(String documentName, String query, float simScore, int rank, int queryId) {
        this.documentName = documentName;
        this.query = query;
        this.simScore = simScore;
        this.rank = rank;
        this.queryId = queryId;
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

    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }
}
