import com.bordercloud.sparql.SparqlClient;
import com.bordercloud.sparql.SparqlClientException;
import com.bordercloud.sparql.SparqlResult;
import com.bordercloud.sparql.SparqlResultModel;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class Example3 {

    // https://linkedwiki.com/query/DBpedia_and_Wikidata_Meshup_---_Galaxies
    public static void main(String[] args) {
        try {
            URI endpoint = new URI("https://query.wikidata.org/sparql");

            //https://linkedwiki.com/query/Geolocation_of_craters_on_Mars
            //language=SPARQL
            String querySelect =
                    "PREFIX bd: <http://www.bigdata.com/rdf#>\n" +
                    "PREFIX p: <http://www.wikidata.org/prop/>\n" +
                    "PREFIX psv: <http://www.wikidata.org/prop/statement/value/>\n" +
                    "PREFIX wd: <http://www.wikidata.org/entity/>\n" +
                    "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" +
                    "PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
                    "\n" +
                    "\n" +
                    "select distinct\n" +
                    "        ?coord ?siteLabel ?siteDescription ?site\n" +
                    "        (concat(xsd:string(?image),'?width=200') as ?newimage)\n" +
                    "where {\n" +
                    "\n" +
                    "?site wdt:P376  wd:Q111 .\n" +
                    "?site wdt:P18 ?image .\n" +
                    "          ?site wdt:P625 ?coord .\n" +
                    "\n" +
                    "SERVICE wikibase:label {\n" +
                    "bd:serviceParam wikibase:language 'en,fr' .\n" +
                    "}\n" +
                    "      } LIMIT 100";

            SparqlClient sc = new SparqlClient(false);
            sc.setEndpointRead(endpoint);
            SparqlResult sr = sc.query(querySelect);
            //sc.printLastQueryAndResult();

            printResult(sr.getModel(), 30);
        } catch (URISyntaxException | SparqlClientException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public static void printResult(SparqlResultModel rs, int size) {
        for (String variable : rs.getVariables()) {
            System.out.print(String.format("%-" + size + "." + size + "s", variable) + " | ");
        }
        System.out.print("\n");
        for (HashMap row : rs.getRows()) {
            for (String variable : rs.getVariables()) {
                System.out.print(String.format("%-" + size + "." + size + "s", row.get(variable)) + " | ");
            }
            System.out.print("\n");
        }
    }
}