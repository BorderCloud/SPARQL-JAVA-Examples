import com.bordercloud.sparql.SparqlClient;
import com.bordercloud.sparql.SparqlClientException;
import com.bordercloud.sparql.SparqlResult;
import com.bordercloud.sparql.SparqlResultModel;

import java.net.URI;
import java.net.URISyntaxException;

public class Example1 {
    public static void main(String[] args) {
        try {
            URI endpoint = new URI("https://query.wikidata.org/sparql");

            String querySelect  =
                    "select  * \n"
                    + "where { \n"
                    + " ?subject ?property ?objectValue . \n"
                    + "}" +
                    "LIMIT 5";

            SparqlClient sc = new SparqlClient(false);
            sc.setEndpointRead(endpoint);
            SparqlResult sr = sc.query(querySelect);
            sc.printLastQueryAndResult();

            SparqlResultModel rows_queryPopulationInFrance = sr.getModel();
            if (rows_queryPopulationInFrance.getRowCount() > 0) {
                System.out.print("Result population in France: " + rows_queryPopulationInFrance.getRows().get(0).get("population"));
            }
        } catch (URISyntaxException | SparqlClientException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}